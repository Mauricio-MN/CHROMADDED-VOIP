
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
        this.mapsManager = mapsManager;
    }

    /**
     * Add pré connected Player by GameServer
     * @param token Token access of player
     * @param id ID access of player
     */
    public synchronized void addPreConnect(long token, int id){
        Player player = new Player(mapsManager);
        player.setConnectionState(PlayerState.WAITING);
        player.setHashMapNb(token);
        player.setID(-2);

        String infoHash = "" + token + "@" + id;
        preConnectedPlayers.put(infoHash, player);
    }

    /**
     * 
     * @param address IP Address from player
     * @param port UDP opened port from player
     * @param Name Name of player
     * @param token Token access of player
     * @param id ID access of player
     */
    public synchronized void addConnect(InetAddress address, int port, String Name, long token, int id) {
        String infoPreConnectedHash = "" + token + "@" + id;
        if (preConnectedPlayers.containsKey(infoPreConnectedHash)) {

            HashInfo info = new HashInfo(address, port, token);
            if (players.containsKey(info.getHash()) == false) {
                Player player = new Player(mapsManager);
                player.setConnectionState(PlayerState.WAITING);
                player.setHashMapNb(token);
                player.setID(id);
                player.setName(Name);

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
