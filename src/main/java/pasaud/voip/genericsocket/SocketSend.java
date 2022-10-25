package pasaud.voip.genericsocket;

import java.net.InetAddress;

public interface SocketSend {
	public void send(InetAddress address, int port, byte[] data);
}