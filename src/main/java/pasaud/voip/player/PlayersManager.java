
package pasaud.voip.player;

import java.util.concurrent.ConcurrentHashMap;

import pasaud.voip.player.hash.PlayerHashInfo;
import pasaud.voip.player.hash.PlayerPreConnectHashInfo;

public class PlayersManager {

    private static ConcurrentHashMap<String, Player> players;
    private static ConcurrentHashMap<String, Player> playerbyname;
    private static ConcurrentHashMap<Integer, Player> playerbyid;
    private static ConcurrentHashMap<String, Player> preConnectedPlayers;

    private PlayersManager() {
    }
    
    public static void init() {
    	players = new ConcurrentHashMap<>();
    	playerbyname = new ConcurrentHashMap<>();
        preConnectedPlayers = new ConcurrentHashMap<>();
    }

    /**
     * Add pré connected Player by GameServer
     * @param token Token access of player;
     * @param id ID access of player;
     * @param name Name of player;
     * @param key decript key for packets;
     */
    public static void addPreConnect(PlayerHashInfo hash, byte[] keycript){
    	if (!preConnectedPlayers.containsKey(hash.getHash()) ) {
	        Player player = new PlayerNormal();
	        player.setConnectionState(PlayerState.WAITING);
	        player.setName(hash.getname());
	        player.setID(hash.getId());
	        player.setHashCodePreConnect(hash);
	        preConnectedPlayers.put(hash.getHash(), player);
	        playerbyname.put(hash.getname(), player);
	        playerbyid.put(hash.getId(), player);
    	}
    }

    /**
     * 
     * @param socket Server Socket;
     * @param address IP Address from player;
     * @param port UDP opened port from player;
     * @param id ID access of player;
     */
    
    public static void addConnect(PlayerHashInfo hash) {
    	PlayerHashInfo possiblePreConnect = 
    			new PlayerPreConnectHashInfo(hash.getRegisterId(), hash.getId());
        if (preConnectedPlayers.containsKey(possiblePreConnect.getHash()) ) {
        	Player player = preConnectedPlayers.get(possiblePreConnect.getHash());
            if (!players.containsKey(hash.getHash())) {
                player.setConnectionState(PlayerState.CONNECTED);
                player.setHashCode(hash);
                players.put(hash.getHash(), player);
            }

        }
    }

    public static void disconnect(PlayerHashInfo hashInfo) {
        String hash = hashInfo.getHash();
        if (players.containsKey(hash)) {
        	Player player = players.get(hash);
        	preConnectedPlayers.remove(player.getHashCodePreConnect().getHash());
        	playerbyid.remove(player.getID());
        	playerbyname.remove(player.getHashCodePreConnect().getname());
            players.remove(hash);
        }
    }

    public static Player getPlayer(PlayerHashInfo hashInfo) {
        String hash = hashInfo.getHash();
        if (players.containsKey(hash)) {
            Player player = players.get(hash);
            return player;
        }
        return null;
    }
    
    public static Player getPlayerByHashString(String hash) {
        if (players.containsKey(hash)) {
            Player player = players.get(hash);
            return player;
        }
        return null;
    }
    
    public static Player getPlayerByName(String name) {
        if (playerbyname.containsKey(name)) {
            Player player = playerbyname.get(name);
            return player;
        }
        return null;
    }
    
    public static Player getPlayerByID(Integer id) {
        if (playerbyid.containsKey(id)) {
            Player player = playerbyid.get(id);
            return player;
        }
        return null;
    }

    public static boolean containsPlayerConnected(PlayerHashInfo hashInfo) {
        if (players.containsKey(hashInfo.getHash())) {
            return true;
        }
        return false;
    }
    
    public static boolean containsPlayerConnectedByHashString(String hash) {
        if (players.containsKey(hash)) {
            return true;
        }
        return false;
    }
    
    public static boolean containsPlayerByName(String name) {
        if (playerbyname.containsKey(name)) {
            return true;
        }
        return false;
    }
}
