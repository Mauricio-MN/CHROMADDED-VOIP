
package pasaud.voip.maps;

import java.util.concurrent.ConcurrentHashMap;

import pasaud.voip.player.Player;

public class Chunk {

	private ConcurrentHashMap<Integer, Player> players;

    public Chunk() {
        players = new ConcurrentHashMap<>();
    }

    public Player[] getPlayers() {
        if (!players.isEmpty()) {
            Player[] playersArray = players.values().toArray(new Player[0]);
            return playersArray;
        }
        Player[] playersArray = new Player[0];
        return playersArray;
    }

    public boolean isEmpty() {
        return players.isEmpty();
    }
    
    public void addPlayer(Player player) {
        if (!players.containsKey(player.getID())) {
            players.put(player.getID(), player);
        }
    }

    public void removePlayer(int id) {
    	if(players.containsKey(id)) {
    		players.remove(id);
    	}
    }

    public boolean havePlayer(int id) {
        if (players.containsKey(id)) {
            return true;
        }
        return false;
    }

}
