package pasaud.voip.types;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class CAddress {
	
	private InetAddress address;
	private Integer port;
	
	public CAddress() {
		this.address = InetAddress.getLoopbackAddress();
		this.port = -1;
	}
	
	public CAddress(InetAddress address, int port){
		this.address = address;
		this.port = port;
	}
	
	public InetAddress getAddress() {
		try {
			return InetAddress.getByAddress(address.getAddress());
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	public void setAddress(InetAddress address) {
		this.address = address;
	}

	public Integer getPort() {
		return port;
	}

	public void setPort(Integer port) {
		this.port = port;
	}



	@Override
	public String toString() {
		return new String(address.getHostAddress() + ":" + port.toString());
	}

}
