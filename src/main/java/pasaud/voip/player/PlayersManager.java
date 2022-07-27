
package pasaud.voip.player;

import java.net.InetAddress;
import java.util.concurrent.ConcurrentHashMap;
import pasaud.voip.Maps.MapsManager;

public class PlayersManager {

    private ConcurrentHashMap<String, Player> players;
    private ConcurrentHashMap<String, Player> preConnectedPlayers;

    private MapsManager mapsManager;

    public PlayersManager(MapsManager mapsManager) {
        this.players = new ConcurrentHashMap();
        this.preConnectedPlayers = new ConcurrentHashMap();
        this.mapsManager = mapsManager;
    }

    /**
     * Add pré connected Player by GameServer
     * @param token Token access of player;
     * @param id ID access of player;
     * @param name Name of player;
     * @param key decript key of packet;
     */
    public synchronized void addPreConnect(long token, int id, String name, byte[] key){
        Player player = new Player(mapsManager);
        player.setConnectionState(PlayerState.WAITING);
        player.setHashMapNb(token);
        player.setName(name);
        player.setID(id);

        String infoHash = "" + id;
        preConnectedPlayers.put(infoHash, player);
    }

    /**
     * 
     * @param address IP Address from player;
     * @param port UDP opened port from player;
     * @param id ID access of player;
     */
    public synchronized void addConnect(InetAddress address, int port, int id) {
        String infoPreConnectedHash = "" + id;
        if (preConnectedPlayers.containsKey(infoPreConnectedHash)) {

            Player playerPreconnected = preConnectedPlayers.get(infoPreConnectedHash);

            HashInfo info = new HashInfo(address, port, playerPreconnected.getHashMapNb());
            if (players.containsKey(info.getHash()) == false) {
                Player player = new Player(mapsManager);
                player.setConnectionState(PlayerState.WAITING);
                player.setID(id);

                players.put(info.getHash(), player);
            }

        }
    }

    public synchronized void disconnect(HashInfo hashInfo) {
        String hash = hashInfo.getHash();
        if (players.containsKey(hash)) {
            players.remove(hash);
        }
    }

    /**
     *
     * @param hashInfo Base Class HashInfo to compute Token + Address to HashString;
     * @param map Map;
     * @param x Coord X;
     * @param y Coord Y;
     * @param z Coord Z;
     */
    public synchronized void setPosition(HashInfo hashInfo, int map, int x, int y, int z) {
        String hash = hashInfo.getHash();
        if (players.containsKey(hash)) {
            Player player = players.get(hash);
            if (player.getMap() != map) player.setMap(map);
            player.setXcoord(x);
            player.setYcoord(y);
            player.setZcoord(z);
        }
    }

    /**
     *
     * @param hashInfo Base Class HashInfo to compute Token + Address to HashString;
     * @param groupId Id of Group to talk;
     */
    public synchronized void setGroup(HashInfo hashInfo, int groupId) {
        String hash = hashInfo.getHash();
        if (players.containsKey(hash)) {
            Player player = players.get(hash);
            player.setGroup(groupId);
        }
    }

    public synchronized PlayerContract getPlayer(HashInfo hashInfo) {
        String hash = hashInfo.getHash();
        if (players.containsKey(hash)) {
            Player player = players.get(hash);
            return player;
        }
        return null;
    }
}
