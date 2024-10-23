package pasaud.voip.protocol;

import java.net.InetAddress;

import pasaud.voip.genericsocket.SendDatagram;
import pasaud.voip.protocol.Protocol.Server;

public class InvalidSession extends SessionInfo{
	
	public InvalidSession(InetAddress address, int port) {
		super(address, port);
	}
	
	public void send() {
		Server.Builder fromServerBuilder = Server.newBuilder();
		Server fromServer = fromServerBuilder.setInvalidSession(true).build();
		sender.send(getAddress(), getPort(), fromServer.toByteArray());
	}
	
}
