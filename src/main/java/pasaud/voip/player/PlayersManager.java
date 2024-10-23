
package pasaud.voip.player;

import java.util.concurrent.ConcurrentHashMap;

import pasaud.voip.types.CAddress;

/**
 * Manager of players online and pre connected's
 */
public class PlayersManager {

    private static ConcurrentHashMap<String, Player> playerbyname;
    private static ConcurrentHashMap<String, Player> playerbyAddress;
    private static ConcurrentHashMap<Integer, Player> players;
    private static ConcurrentHashMap<Integer, Player> preConnectedPlayers;
    private static ConcurrentHashMap<Integer, Player> preConnectedPlayerBySecretId;

    private PlayersManager() {
    }
    
    public static void init() {
    	players = new ConcurrentHashMap<>();
    	playerbyname = new ConcurrentHashMap<>();
        preConnectedPlayers = new ConcurrentHashMap<>();
        playerbyAddress = new ConcurrentHashMap<>();
        preConnectedPlayerBySecretId = new ConcurrentHashMap<>();
     
    }
    

    /**
     * Add pre connected Player by API
     */
    public static void addPreConnect(Player player){
    	if (!preConnectedPlayers.containsKey(player.getID()) ) {
	        player.setConnectionState(PlayerState.WAITING);
	        preConnectedPlayers.put(player.getID(), player);
	        playerbyname.put(player.getPublicId(), player);
	        preConnectedPlayerBySecretId.put(player.getScretID(), player);
    	}
    }
    
    /**
     * Add connected Player by client (first 'addPreConnect')
     */
    public static Boolean addConnect(Player player) {	
    	
        if (preConnectedPlayers.containsKey(player.getID()) ) {
            if (!players.containsKey(player.getID())) {
                player.setConnectionState(PlayerState.CONNECTED);
                players.put(player.getID(), player);
                CAddress caddress = new CAddress(player.getAddress(), player.getPort());
                playerbyAddress.put(caddress.toString(), player);
            }
            preConnectedPlayers.remove(player.getID());
            preConnectedPlayerBySecretId.remove(player.getScretID());
            return true;
        } else {
        	return false;
        }
    }

    /**
     * Remove connected Player
     * @param player_id public id of player
     */
    public static void disconnect(Integer player_id) {
        if (players.containsKey(player_id)) {
        	Player playerRef = players.get(player_id);
        	playerbyname.remove(playerRef.getPublicId());
        	CAddress caddress = new CAddress(playerRef.getAddress(), playerRef.getPort());
            playerbyAddress.remove(caddress.toString());
            players.remove(player_id);
            playerRef.removeFromMap();
        }
        if (preConnectedPlayers.containsKey(player_id)) {
        	Player playerRef = preConnectedPlayers.get(player_id);
        	if(playerbyname.containsKey(playerRef.getPublicId()) ) {
        		playerbyname.remove(playerRef.getPublicId());
        	}
        	preConnectedPlayerBySecretId.remove(playerRef.getScretID());
        	preConnectedPlayers.remove(player_id);
        }
    }

    /**
     * Get connected Player by hash
     * @param hashInfo connect hash;
     */
    public static Player getPlayer(Integer player_id) {
        if (players.containsKey(player_id)) {
            Player player = players.get(player_id);
            return player;
        }
        return null;
    }
    
    public static Player getPlayerPreConnect(Integer player_id) {
        if (preConnectedPlayers.containsKey(player_id)) {
            Player player = preConnectedPlayers.get(player_id);
            return player;
        }
        return null;
    }
    
    /**
     * Get connected Player by name
     * @param name player name;
     */
    public static Player getPlayerByName(String name) {
        if (playerbyname.containsKey(name)) {
            Player player = playerbyname.get(name);
            return player;
        }
        return null;
    }
    
    public static Player getPlayerByAddress(CAddress caddress) {
    	String addressStr = caddress.toString();
        if (playerbyAddress.containsKey(addressStr)) {
            Player player = playerbyAddress.get(addressStr);
            return player;
        }
        return null;
    }
    
    public static Player getPreConnectedPlayerByScretId(Integer scretet_id) {
        if (preConnectedPlayerBySecretId.containsKey(scretet_id)) {
            Player player = preConnectedPlayerBySecretId.get(scretet_id);
            return player;
        }
        return null;
    }

    /**
     * Verify connect state of player by hash info;
     * @param hashInfo player hash;
     * @return return TRUE if is connect, else return FALSE;
     */
    public static boolean containsPlayerConnected(Integer player_id) {
        if (players.containsKey(player_id)) {
            return true;
        }
        return false;
    }
    
    public static boolean containsPreConnectedPlayer(Integer player_id) {
        if (preConnectedPlayers.containsKey(player_id)) {
            return true;
        }
        return false;
    }
    
    public static boolean containsPreConnectedPlayerBySecretId(Integer scretet_id) {
        if (preConnectedPlayerBySecretId.containsKey(scretet_id)) {
            return true;
        }
        return false;
    }
    
    /**
     * Verify state of player by name, NOT use for checks;
     * @param name player name;
     */
    public static boolean containsPlayerByName(String name) {
        if (playerbyname.containsKey(name)) {
            return true;
        }
        return false;
    }
    
    public static boolean containsPlayerByAddress(CAddress caddress) {
        if (playerbyAddress.containsKey(caddress.toString())) {
            return true;
        }
        return false;
    }
}
