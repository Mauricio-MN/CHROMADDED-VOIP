package pasaud.voip.player.hash;

import java.net.InetAddress;

public class PlayerConnectHashInfo extends PlayerHashInfo {
    
    public PlayerConnectHashInfo(Integer reg_id, Integer id, InetAddress address, Integer port) {
    	super();
    	this.register_id = reg_id;
    	this.id = id;
    	this.address = address;
    	this.type = HashType.CONNECT;
    	this.port = port;
    	
    }
    
    public String getHash(){
        String result = address.getHostName() + port + id;
        return result;
    }
    
}
