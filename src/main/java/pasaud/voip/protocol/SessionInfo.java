package pasaud.voip.protocol;

import java.net.InetAddress;

import pasaud.voip.genericsocket.SendDatagram;

public abstract class SessionInfo {
	private InetAddress address;
	private int port;
	
	private int id;
	
	SendDatagram sender;

	public SessionInfo(InetAddress address, int port){
		this.setAddress(address);
		this.setPort(port);
		this.id = -1;
		sender = new SendDatagram();
	}
	
	public abstract void send();

	public InetAddress getAddress() {
		return address;
	}

	public void setAddress(InetAddress address) {
		this.address = address;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
}
