
package pasaud.voip;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;


public class UDPServer{
    public static DatagramSocket serverSocket;

    private UDPServer(){
    }
    
    public static void init(int port) throws IOException{
    	serverSocket = new DatagramSocket(port);
        //Cipher cipher = Cipher.getInstance("AES/CTR/NoPadding");
        //cipher.
        new Thread(new Handler()).start();
    }

}

  class Handler implements Runnable {

      Handler() {
      }

      @Override
      public void run() {
          try {
              while (!UDPServer.serverSocket.isClosed()) {

                  byte[] buffer = new byte[256]; // code not shown
                  DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                  UDPServer.serverSocket.receive(packet);

                  System.out.println("Received UDP packet.");

                  new Thread(new ClientBufferParser(packet)).start();

              }
          } catch (IOException e) {
              Thread t = Thread.currentThread();
              t.getUncaughtExceptionHandler().uncaughtException(t, e);
          }
      }
 }
