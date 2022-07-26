package pasaud.voip.player;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;
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

    private int groupId;

    private PlayerState connectionState;

    private Queue<PlayerPacketAudio> receivedAudio = new LinkedList<>();


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
        this.groupId = -1;


}

    @Override
    public int getMap() {
        return map;
    }

    @Override
    public int getXcoord() {
        return x;
    }

    @Override
    public int getYcoord() {
        return y;
    }

    @Override
    public int getZcoord() {
        return z;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public int getID() {
        return id;
    }

    @Override
    public long getHashMapNb() {
        return numberMapHash;
    }

    @Override
    public boolean getIsGroupTalk() {
        return groupId != -1;
    }

    @Override
    public void setMap(int map) {
        if (map != this.map) {
            externMapManager.getMap(this.map).getChunkByCoords(x, y, z).removePlayer(this);
            this.map = map;
            externMapManager.getMap(this.map).getChunkByCoords(x, y, z).addPlayer(this);
        }
    }

    @Override
    public void setXcoord(int x) {
        if (!externMapManager.getMap(this.map).getChunkByCoords(x, this.y, this.z).havePlayer(this)) {
            externMapManager.getMap(this.map).getChunkByCoords(this.x, this.y, this.z).removePlayer(this);
            this.x = x;
            externMapManager.getMap(this.map).getChunkByCoords(this.x, this.y, this.z).addPlayer(this);
        } else {
            this.x = x;
        }
    }

    @Override
    public void setYcoord(int y) {
        if (!externMapManager.getMap(this.map).getChunkByCoords(this.x, y, this.z).havePlayer(this)) {
            externMapManager.getMap(this.map).getChunkByCoords(this.x, this.y, this.z).removePlayer(this);
            this.y = y;
            externMapManager.getMap(this.map).getChunkByCoords(this.x, this.y, this.z).addPlayer(this);
        } else {
            this.y = y;
        }
    }

    @Override
    public void setZcoord(int z) {
        if (!externMapManager.getMap(this.map).getChunkByCoords(this.x, this.y, z).havePlayer(this)) {
            externMapManager.getMap(this.map).getChunkByCoords(this.x, this.y, this.z).removePlayer(this);
            this.z = z;
            externMapManager.getMap(this.map).getChunkByCoords(this.x, this.y, this.z).addPlayer(this);
        } else {
            this.z = z;
        }
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public void setID(int id) {
        this.id = id;
    }

    @Override
    public void setHashMapNb(long numberMapHash) {
        this.numberMapHash = numberMapHash;
    }

    @Override
    public void setGroup(int id) {
        this.groupId = id;
    }

    @Override
    public PlayerState getState() {
        return connectionState;
    }

    @Override
    public void setConnectionState(PlayerState state) {
        this.connectionState = state;
    }

    @Override
    public void sendToGroup(byte[] audio) {
        if (this.getIsGroupTalk()) {

        }
    }

    @Override
    public void sendToGeral(byte[] audio) {
        PlayerContract[] players = this.externMapManager.getMap(this.map).getChunkByCoords(x, y, z).getPlayers();
        for (PlayerContract player : players) {
            PlayerPacketAudio packet = new PlayerPacketAudio(this, audio);
            player.receiveFromGeral(packet);
        }
    }

    @Override
    public void receiveFromGroup(PlayerPacketAudio packet) {

    }

    @Override
    public void receiveFromGeral(PlayerPacketAudio packet) {
        receivedAudio.add(packet);
    }

    @Override
    public PlayerPacketAudio udpBufferQueueClean(){
        return receivedAudio.poll();
    }

}
