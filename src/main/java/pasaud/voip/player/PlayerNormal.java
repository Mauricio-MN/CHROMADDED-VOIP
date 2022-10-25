package pasaud.voip.player;

import pasaud.voip.player.audio.PlayerPacketAudio;
import pasaud.voip.player.hash.PlayerHashInfo;
import pasaud.voip.protocol.udp.ProtocolToClient;
import pasaud.voip.genericsocket.PlayersSocket;
import pasaud.voip.maps.MapsManager;
import pasaud.voip.player.audio.PlayerAudioType;

public class PlayerNormal implements Player {

    private PlayerHashInfo hashCode;
    private PlayerHashInfo hashCodePreConnect;

    private int x;

    private int y;

    private int z;

    private int map;

    private int id;

    private String name;

    private boolean groupParticipate;
    private int groupId;
    
    private int packetNumber;

    private PlayerState connectionState;
    
    private byte[] cryptoKey;

    public PlayerNormal() {

        this.x = -1;
        this.y = -1;
        this.z = -1;
        this.map = -1;
        this.id = -1;
        this.name = "";
        this.connectionState = PlayerState.EMPTY;
        this.groupParticipate = false;
        this.groupId = -1;
        this.cryptoKey = new byte[16];

    }

    @Override
    public synchronized int getMap() {
        return map;
    }

    @Override
    public synchronized int getXcoord() {
        return x;
    }

    @Override
    public synchronized int getYcoord() {
        return y;
    }

    @Override
    public synchronized int getZcoord() {
        return z;
    }

    @Override
    public synchronized String getName() {
        return name;
    }

    @Override
    public synchronized int getID() {
        return id;
    }

    @Override
    public synchronized boolean getIsGroupTalk() {
        return groupParticipate;
    }

    @Override
    public void setHashCode(PlayerHashInfo hash){
        this.hashCode = hash;
    }

    @Override
    public PlayerHashInfo getHashCode(){
        return this.hashCode;
    }
    
    @Override
	public void setHashCodePreConnect(PlayerHashInfo hash) {
    	this.hashCodePreConnect = hash;
    }

    @Override
	public PlayerHashInfo getHashCodePreConnect() {
    	return this.hashCodePreConnect;
    }

    @Override
    public synchronized void setMap(int map) {
        if (map != this.map) {
            if (MapsManager.getMap(this.map) != null) {
                MapsManager.getMap(this.map).removePlayer(this);
                MapsManager.getMap(this.map).getChunkByCoords(x, y, z).removePlayer(this);
            }
            this.map = map;
            MapsManager.getMap(this.map).addPlayer(this);
            MapsManager.getMap(this.map).getChunkByCoords(x, y, z).addPlayer(this);
        }
    }

    @Override
    public synchronized void setXcoord(int x) {
        if (!MapsManager.getMap(this.map).getChunkByCoords(x, this.y, this.z).havePlayer(this)) {
            MapsManager.getMap(this.map).getChunkByCoords(this.x, this.y, this.z).removePlayer(this);
            this.x = x;
            MapsManager.getMap(this.map).getChunkByCoords(this.x, this.y, this.z).addPlayer(this);
        } else {
            this.x = x;
        }
    }

    @Override
    public synchronized void setYcoord(int y) {
        if (!MapsManager.getMap(this.map).getChunkByCoords(this.x, y, this.z).havePlayer(this)) {
            MapsManager.getMap(this.map).getChunkByCoords(this.x, this.y, this.z).removePlayer(this);
            this.y = y;
            MapsManager.getMap(this.map).getChunkByCoords(this.x, this.y, this.z).addPlayer(this);
        } else {
            this.y = y;
        }
    }

    @Override
    public synchronized void setZcoord(int z) {
        if (!MapsManager.getMap(this.map).getChunkByCoords(this.x, this.y, z).havePlayer(this)) {
            MapsManager.getMap(this.map).getChunkByCoords(this.x, this.y, this.z).removePlayer(this);
            this.z = z;
            MapsManager.getMap(this.map).getChunkByCoords(this.x, this.y, this.z).addPlayer(this);
        } else {
            this.z = z;
        }
    }
    
    @Override
    public synchronized void setPosition(int map, int x, int y, int z){
    	this.setMap(map);
    	this.setYcoord(y);
    	this.setXcoord(x);
    	this.setZcoord(z);
    }

    @Override
    public synchronized void setName(String name) {
        this.name = name;
    }

    @Override
    public synchronized void setID(int id) {
        this.id = id;
    }

    @Override
    public synchronized void setGroupTalking(boolean isTalking) {
        this.groupParticipate = isTalking;
    }


    @Override
    public synchronized void setGroup(int id) {
        this.groupId = id;
    }

    @Override
    public synchronized PlayerState getState() {
        return connectionState;
    }

    @Override
    public synchronized void setConnectionState(PlayerState state) {
        this.connectionState = state;
    }

    @Override
    public PlayerPacketAudio packetMyAudio(PlayerAudioType audioType, Integer number, byte[] audio){
        PlayerPacketAudio packet = new PlayerPacketAudio(this.id, this.hashCode.getHash(), number, audio, audioType);
        return packet;
    }

    @Override
    public boolean sendPacketToMe(PlayerAudioType audioType, PlayerPacketAudio packet){
    	if(groupParticipate && audioType == PlayerAudioType.TOGROUP) {
    		
    		return true;
    	} else if(groupParticipate == false && audioType == PlayerAudioType.TOGERAL) {
    		byte[] data = ProtocolToClient.createAudioInfo(packet.getID(), packet.getNumber(), packet.getAudio());
    		PlayersSocket.send(hashCode.getAddress(), hashCode.getPort(), data);
    		return true;
    	}
        return false;
    }
    
    @Override
    public boolean isValidNumberPacketAudio(int nb) {
    	if(nb > packetNumber || (packetNumber == 255 && nb < 35)) {
    		packetNumber = nb;
    		return true;
    	}
    	return false;
    }

}
