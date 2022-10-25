package pasaud.voip.player.hash;

public class PlayerPreConnectHashInfo extends PlayerHashInfo {

    public PlayerPreConnectHashInfo(Integer reg_id, Integer id){
    	super();
    	this.register_id = reg_id;
    	this.id = id;
    	this.type = HashType.PRECONNECT;
    }
    
    public String getHash() {
    	String hash = "" + register_id + "" + id;
    	return hash;
    }
    
}
