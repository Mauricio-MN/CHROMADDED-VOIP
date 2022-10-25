package pasaud.voip.genericsocket;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;

import pasaud.voip.UDPServer;

public class SendDatagram implements SocketSend {
	
	public SendDatagram() {
		
	}
	
	@Override
	public void send(InetAddress address, int port, byte[] data) {
		try {
			DatagramPacket packet = new DatagramPacket(data, data.length, address, port);
			UDPServer.serverSocket.send(packet);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
