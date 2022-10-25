
package pasaud.voip.player.hash;

import java.net.InetAddress;


public abstract class PlayerHashInfo {

	protected Integer register_id;
    protected Integer id;
    protected InetAddress address;
    protected int port;
    protected String name;
    protected HashType type;
    
    public PlayerHashInfo(){
    	name = "";
    }

    public abstract String getHash();
    
    public int getId() {
        return id;
    }
    
    public int getRegisterId() {
        return register_id;
    }
    
    public String getname(){
    	return name;
    }
    
    public InetAddress getAddress() {
    	return address;
    }
    
    public int getPort() {
    	return port;
    }

}
