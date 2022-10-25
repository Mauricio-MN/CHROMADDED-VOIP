package pasaud.voip.genericsocket;

import java.net.InetAddress;

public class PlayersSocket {
	
	private static GenericSocketType type;
	private static SocketSend sender;
	
	
	public static void init(GenericSocketType type) {
		PlayersSocket.type = type;
		if(type == GenericSocketType.DATAGRAM) {
			sender = new SendDatagram();
		} else {
			sender = new SendDatagram();
		}
	}
	
	public static void send(InetAddress address, int port, byte[] data) {
		sender.send(address, port, data);
	}
	
	public static GenericSocketType getType() {
		return type;
	}
	
	

}
