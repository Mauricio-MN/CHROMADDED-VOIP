
package pasaud.voip;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import pasaud.voip.player.PlayersManager;

public class TCPServer {

ServerSocket server;

    public TCPServer(PlayersManager playersManager) {
        try {
            server = new ServerSocket(909001);
            System.out.println("Servidor ouvindo a porta 909001");

            Socket client = server.accept();
            System.out.println("Cliente conectado: " + client.getInetAddress().getHostAddress());

            new Thread(new HandlerTCP(server, client, playersManager)).start();
            
        } catch (Exception e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }
}

class HandlerTCP implements Runnable {

    private final ServerSocket server;
    private PlayersManager playersManager;
    private Socket client;

      HandlerTCP(ServerSocket socket, Socket client, PlayersManager playersManager) {
          this.server = socket;
          this.playersManager = playersManager;
          this.client = client;
      }

    @Override
    public void run() {
        try {

            ObjectInputStream input = new ObjectInputStream(client.getInputStream());
            int size = input.available();
            byte[] data = new byte[size];
            input.readFully(data);

            //ObjectOutputStream saida = new ObjectOutputStream(client.getOutputStream());
            //saida.flush();
            //saida.writeObject(new Date());
            //saida.close();

            if (!client.isClosed()) {
                run();
            }
        } catch (IOException e) {
            Thread t = Thread.currentThread();
            t.getUncaughtExceptionHandler().uncaughtException(t, e);
        }
    }
}
