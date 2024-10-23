package pasaud.voip.muonline.database;

public class VoiceDTO {
	
	private int id;
	private int token;
	private int room;
	private String cryptoKey;
	private int map;
	private boolean talkLocal;
	private boolean talkGroup;
	private boolean listenLocal;
	private boolean listenGroup;

	public VoiceDTO() {
		id = -1;
		token = -1;
		room = -1;
		cryptoKey = new String("");
		map = 0;
		talkLocal = true;
		talkGroup = false;
		listenLocal = true;
		listenGroup = false;
	}
	
	public boolean isValid() {
		if(id != -1 && token != -1) return true;
		return false;
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getToken() {
		return token;
	}

	public void setToken(int token) {
		this.token = token;
	}
	
	public int getRoom() {
		return room;
	}

	public void setRoom(int room) {
		this.room = room;
	}

	public String getCryptoKey() {
		return cryptoKey;
	}

	public void setCryptoKey(String cryptoKey) {
		this.cryptoKey = cryptoKey;
	}
	
	public String getString() {
		return ""+id+""+token+""+room+cryptoKey;
	}
	
	public int getMap() {
		return map;
	}

	public void setMap(int map) {
		this.map = map;
	}
	
	public boolean isTalkLocal() {
		return talkLocal;
	}

	public void setTalkLocal(boolean talkLocal) {
		this.talkLocal = talkLocal;
	}

	public boolean isTalkGroup() {
		return talkGroup;
	}

	public void setTalkGroup(boolean talkGroup) {
		this.talkGroup = talkGroup;
	}

	public boolean isListenLocal() {
		return listenLocal;
	}

	public void setListenLocal(boolean listenLocal) {
		this.listenLocal = listenLocal;
	}

	public boolean isListenGroup() {
		return listenGroup;
	}

	public void setListenGroup(boolean listenGroup) {
		this.listenGroup = listenGroup;
	}
	
}
