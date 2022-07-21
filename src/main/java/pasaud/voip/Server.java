
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


class Server{
    private final DatagramSocket serverSocket;

    public Server(int port, int poolSize) throws IOException {
      serverSocket = new DatagramSocket(port);
      new Thread(new Handler(serverSocket)).start();
    }

}

  class Handler implements Runnable {
    private final DatagramSocket socket;

      Handler(DatagramSocket socket) {
          this.socket = socket;
      }

      @Override
      public void run() {
          try {
              byte[] buffer = new byte[]{0, 1, 02, 03}; // code not shown
              DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
              socket.receive(packet);

              InetAddress clientAddress = packet.getAddress();
              int clientPort = packet.getPort();
              new Thread(new ClientBufferParser(buffer, buffer.length, clientAddress, clientPort)).start();

              socket.send(packet);
              if(!socket.isClosed()) run();
          } catch (IOException e) {
              Thread t = Thread.currentThread();
              t.getUncaughtExceptionHandler().uncaughtException(t, e);
          }
      }
 }