
package pasaud.voip;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import pasaud.voip.Maps.MapsManager;
import pasaud.voip.player.PlayersManager;


class Server{
    private final DatagramSocket serverSocket;

    public Server(int port, PlayersManager playersManager) throws IOException {
      serverSocket = new DatagramSocket(port);
      new Thread(new Handler(serverSocket, playersManager)).start();
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

              InetAddress clientAddress = packet.getAddress();
              int clientPort = packet.getPort();
              new Thread(new ClientBufferParser(packet)).start();

              socket.send(packet);
              if(!socket.isClosed()) run();
          } catch (IOException e) {
              Thread t = Thread.currentThread();
              t.getUncaughtExceptionHandler().uncaughtException(t, e);
          }
      }
 }