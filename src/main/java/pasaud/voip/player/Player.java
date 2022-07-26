package pasaud.voip.player;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import pasaud.voip.Maps.Map;
import pasaud.voip.Maps.MapsManager;

public class Player implements PlayerContract {

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

    private ConcurrentLinkedQueue<PlayerPacketAudio> receivedAudio = new ConcurrentLinkedQueue<>();
    private ConcurrentLinkedQueue<PlayerPacketAudio> receivedAudioGroup = new ConcurrentLinkedQueue<>();


    private MapsManager externMapManager;

    Player(MapsManager externMapManager) {

        this.externMapManager = externMapManager;

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
    public synchronized void setMap(int map) {
        if (map != this.map) {
            externMapManager.getMap(this.map).getChunkByCoords(x, y, z).removePlayer(this);
            this.map = map;
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

    private void sendToGroup(byte[] audio) {
        
    }

    public synchronized void sendAudio(byte[] audio) {
        if (this.getIsGroupTalk() == false) {
            PlayerContract[] players = this.externMapManager.getMap(this.map).getChunkByCoords(x, y, z).getPlayers();
            for (PlayerContract player : players) {
                PlayerPacketAudio packet = new PlayerPacketAudio(this, audio);
                player.receiveFromGeral(packet);
            }
        } else {
            sendToGroup(audio);
        }
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
