
package pasaud.voip.rooms;

import java.util.concurrent.ConcurrentHashMap;

import pasaud.voip.player.Player;

public class Room {

    private int roomId;
    private ConcurrentHashMap<Integer, Player> players;
    private ConcurrentHashMap<Integer, Boolean> allowedPlayers;

    public Room(int roomId) {
        players = new ConcurrentHashMap<>();
        allowedPlayers = new ConcurrentHashMap<>();
        this.roomId = roomId;
    }
    
    public void addAllowedPlayer(Integer id) {
    	if(!haveAllowedPlayer(id)) {
    		allowedPlayers.put(id, true);
    	}
    }
    
    public void removeAllowedPlayer(Integer id) {
    	allowedPlayers.remove(id);
    }
    
    public synchronized boolean haveAllowedPlayer(Integer id) {
        if (allowedPlayers.containsKey(id)) {
            return true;
        }
        return false;
    }


    public void addPlayer(Integer id, Player player) {
    	if(!haveAllowedPlayer(id)) {
    		return;
    	}
    	
    	if(!havePlayer(id)) {
    		players.put(id, player);
    	}
    }

    public void removePlayer(Integer id) {
        players.remove(id);
    }
    
    public void clearRoom() {
    	players.clear();
    }

    public Player[] getPlayers() {
        Player[] playersArray = (Player[]) players.values().toArray();
        return playersArray;
    }
    
    public synchronized boolean havePlayer(Integer id) {
        if (players.containsKey(id)) {
            return true;
        }
        return false;
    }
    
    public int getID() {
    	return roomId;
    }

}
