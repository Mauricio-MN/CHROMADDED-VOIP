
package pasaud.voip;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import pasaud.voip.Maps.Chunk;
import pasaud.voip.Maps.Map;
import pasaud.voip.Maps.MapsManager;
import pasaud.voip.player.PlayerContract;
import pasaud.voip.player.PlayerPacketAudio;
import pasaud.voip.player.PlayersManager;


class Server{
    private final DatagramSocket serverSocket;

    public Server(int port, PlayersManager playersManager, MapsManager mapsManager, DivisionOfThreads threadType) throws IOException {
        serverSocket = new DatagramSocket(port);
        //Cipher cipher = Cipher.getInstance("AES/CTR/NoPadding");
        //cipher.
        new Thread(new Handler(serverSocket, playersManager)).start();

        for(Map map : mapsManager.getMaps()){
            new Thread(new HandlerPlayersThreadsByMaps(serverSocket, playersManager, map)).start();
        }
    }

}

  class Handler implements Runnable {
      private final DatagramSocket socket;
      private PlayersManager playersManager;

      Handler(DatagramSocket socket, PlayersManager playersManager) {
          this.socket = socket;
          this.playersManager = playersManager;
      }

      @Override
      public void run() {
          try {
              while (!socket.isClosed()) {

                  byte[] buffer = new byte[256]; // code not shown
                  DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                  socket.receive(packet);

                  System.out.println("Received UDP packet.");

                  InetAddress clientAddress = packet.getAddress();
                  int clientPort = packet.getPort();

                  new Thread(new ClientBufferParser(socket, packet, playersManager)).start();

              }
          } catch (IOException e) {
              Thread t = Thread.currentThread();
              t.getUncaughtExceptionHandler().uncaughtException(t, e);
          }
      }
 }

class HandlerPlayersThreadsByMaps implements Runnable {

    private final DatagramSocket socket;
    private PlayersManager playersManager;
    private Map map;

    HandlerPlayersThreadsByMaps(DatagramSocket socket, PlayersManager playersManager, Map map) {
        this.socket = socket;
        this.playersManager = playersManager;
        this.map = map;
    }

    public static int distanceTo(int x, int y, int z, int toX, int toY, int toZ) {
        return (int) Math.sqrt(Math.pow(x - toX, 2) + Math.pow(y - toY, 2) + Math.pow(z - toZ, 2));
    }

    @Override
    public void run() {
        while (!socket.isClosed()) {

            if (!map.isEmpty()) {
                for (PlayerContract player : map.getPlayers()) {
                    PlayerPacketAudio packetToPlayer = player.udpBufferQueueClean();
                    //Construct Buffer Server to Client;
                    PlayerPacketAudio packet = player.unQueueMyPacket();
                    int x = player.getXcoord();
                    int y = player.getYcoord();
                    int z = player.getZcoord();
                    Chunk centerChunk = map.getChunkByCoords(x, y, z);
                    int[] coordsChunk = map.getChunkCoordsByPlayerCoords(x, y, z);

                    for (int rx = -1; rx <= 1; rx++) {
                        for (int ry = -1; ry <= 1; ry++) {
                            for (int rz = -1; rz <= 1; rz++) {
                                Chunk actualChunk = map.getChunk(coordsChunk[0] + rx, coordsChunk[1] + ry, coordsChunk[2] + rz);
                                for (PlayerContract playerinChunk : actualChunk.getPlayers()) {
                                    int vX = playerinChunk.getXcoord();
                                    int vY = playerinChunk.getYcoord();
                                    int vZ = playerinChunk.getZcoord();
                                    int distance = distanceTo(x, y, z, vX, vY, vZ);
                                    if (distance < 10 && distance > -10) {
                                        playerinChunk.receiveFromGeral(packet);
                                    }
                                }
                            }
                        }
                    }

                }
            }
        }

    }

}
