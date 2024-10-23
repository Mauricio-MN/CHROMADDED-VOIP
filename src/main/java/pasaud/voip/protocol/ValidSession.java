package pasaud.voip.protocol;

import java.net.InetAddress;
import java.time.Instant;

import pasaud.voip.protocol.Protocol.Server;

public class ValidSession extends SessionInfo{

	public ValidSession(InetAddress address, int port) {
		super(address, port);
	}
	
	public void send() {
		Server.Builder fromServerBuilder = Server.newBuilder();
		
		Instant currentTime = Instant.now();
		com.google.protobuf.Timestamp.Builder timebuilder = com.google.protobuf.Timestamp.newBuilder();
		timebuilder.setSeconds(currentTime.getEpochSecond());
		timebuilder.setNanos(currentTime.getNano());
		fromServerBuilder.setPacketTime(timebuilder.build());
		
		Server fromServer = fromServerBuilder.setId(getId()).setHandShake(true).build();
		sender.send(getAddress(), getPort(), fromServer.toByteArray());
	}

}
