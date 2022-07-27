
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
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import pasaud.voip.Maps.Map;
import pasaud.voip.Maps.MapsManager;
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
              byte[] buffer = new byte[256]; // code not shown
              DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
              socket.receive(packet);

              System.out.println("Received UDP packet.");

              InetAddress clientAddress = packet.getAddress();
              int clientPort = packet.getPort();

              new Thread(new ClientBufferParser(socket, packet, playersManager)).start();

              if(!socket.isClosed()) run();
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

    @Override
    public void run() {
        try {

            byte[] buffer = new byte[256]; // code not shown
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
            socket.receive(packet);

            System.out.println("Received UDP packet.");

            InetAddress clientAddress = packet.getAddress();
            int clientPort = packet.getPort();

            new Thread(new ClientBufferParser(socket, packet, playersManager)).start();

            if (!socket.isClosed()) {
                run();
            }
        } catch (IOException e) {
            Thread t = Thread.currentThread();
            t.getUncaughtExceptionHandler().uncaughtException(t, e);
        }
    }
}
