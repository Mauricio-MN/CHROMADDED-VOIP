package pasaud.voip.player;

import java.util.concurrent.ConcurrentLinkedQueue;
import pasaud.voip.Maps.MapsManager;

public class Player implements PlayerContract {

    private byte[] key;

    private int x;

    private int y;

    private int z;

    private int map;

    private long numberMapHash;

    private int id;

    private String name;

    private boolean groupParticipate;
    private int groupId;

    private PlayerState connectionState;

    private ConcurrentLinkedQueue<PlayerPacketAudio> myAudio = new ConcurrentLinkedQueue<>();

    private ConcurrentLinkedQueue<PlayerPacketAudio> receivedAudio = new ConcurrentLinkedQueue<>();


    private MapsManager externMapManager;

    Player(MapsManager externMapManager) {

        this.externMapManager = externMapManager;

        this.key = new byte[16];
        this.x = -1;
        this.y = -1;
        this.z = -1;
        this.map = -1;
        this.numberMapHash = -1;
        this.id = -1;
        this.name = "";
        this.connectionState = PlayerState.EMPTY;
        this.groupParticipate = false;
        this.groupId = -1;


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
    public synchronized long getHashMapNb() {
        return numberMapHash;
    }

    @Override
    public synchronized boolean getIsGroupTalk() {
        return groupParticipate;
    }

    @Override
    public void setKey(byte[] key){
        this.key = key;
    }

    @Override
    public synchronized void setMap(int map) {
        if (map != this.map) {
            if (externMapManager.getMap(this.map) != null) {
                externMapManager.getMap(this.map).removePlayer(this);
                externMapManager.getMap(this.map).getChunkByCoords(x, y, z).removePlayer(this);
            }
            this.map = map;
            externMapManager.getMap(this.map).addPlayer(this);
            externMapManager.getMap(this.map).getChunkByCoords(x, y, z).addPlayer(this);
        }
    }

    @Override
    public synchronized void setXcoord(int x) {
        if (!externMapManager.getMap(this.map).getChunkByCoords(x, this.y, this.z).havePlayer(this)) {
            externMapManager.getMap(this.map).getChunkByCoords(this.x, this.y, this.z).removePlayer(this);
            this.x = x;
            externMapManager.getMap(this.map).getChunkByCoords(this.x, this.y, this.z).addPlayer(this);
        } else {
            this.x = x;
        }
    }

    @Override
    public synchronized void setYcoord(int y) {
        if (!externMapManager.getMap(this.map).getChunkByCoords(this.x, y, this.z).havePlayer(this)) {
            externMapManager.getMap(this.map).getChunkByCoords(this.x, this.y, this.z).removePlayer(this);
            this.y = y;
            externMapManager.getMap(this.map).getChunkByCoords(this.x, this.y, this.z).addPlayer(this);
        } else {
            this.y = y;
        }
    }

    @Override
    public synchronized void setZcoord(int z) {
        if (!externMapManager.getMap(this.map).getChunkByCoords(this.x, this.y, z).havePlayer(this)) {
            externMapManager.getMap(this.map).getChunkByCoords(this.x, this.y, this.z).removePlayer(this);
            this.z = z;
            externMapManager.getMap(this.map).getChunkByCoords(this.x, this.y, this.z).addPlayer(this);
        } else {
            this.z = z;
        }
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
    public synchronized void setHashMapNb(long numberMapHash) {
        this.numberMapHash = numberMapHash;
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
    public synchronized void queueMyPacket(byte[] audio) {
        PlayerPacketAudio packet = new PlayerPacketAudio(this, audio);
        myAudio.add(packet);
    }

    @Override
    public synchronized PlayerPacketAudio unQueueMyPacket() {
        return myAudio.poll();
    }

    @Override
    public synchronized void receiveFromGroup(PlayerPacketAudio packet) {

    }

    @Override
    public synchronized void receiveFromGeral(PlayerPacketAudio packet) {
        receivedAudio.add(packet);
    }

    @Override
    public synchronized PlayerPacketAudio udpBufferQueueClean(){
        return receivedAudio.poll();
    }

}
