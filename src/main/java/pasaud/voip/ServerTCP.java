
package pasaud.voip;

import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;

public class ServerTCP {
  public static void main(String[] args) {
    try {
      // Instancia o ServerSocket ouvindo a porta 12345
      ServerSocket servidor = new ServerSocket(909001);
      System.out.println("Servidor ouvindo a porta 909001");
      while(true) {
        // o método accept() bloqueia a execução até que
        // o servidor receba um pedido de conexão
        Socket cliente = servidor.accept();
        System.out.println("Cliente conectado: " + cliente.getInetAddress().getHostAddress());
        ObjectOutputStream saida = new ObjectOutputStream(cliente.getOutputStream());
        saida.flush();
        saida.writeObject(new Date());
        saida.close();
        cliente.close();
      }
    }
    catch(Exception e) {
       System.out.println("Erro: " + e.getMessage());
    }
  }
}