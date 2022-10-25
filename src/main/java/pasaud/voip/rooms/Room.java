
package pasaud.voip.rooms;

import java.util.LinkedList;
import pasaud.voip.player.Player;

public class Room {

    private int id;
    private LinkedList<Player> players;

    public Room(int id) {
        players = new LinkedList<>();
        this.id = id;
    }

    public void resetGroup() {
        players.clear();
    }

    public void addPlayer(Player player) {
        players.add(player);
    }

    public void removePlayer(Player player) {
        players.remove(player);
    }
    
    public void clearRoom() {
    	players.clear();
    }

    public synchronized Player[] getPlayers() {
        Player[] playersArray = (Player[]) players.toArray();
        return playersArray;
    }
    
    public synchronized boolean havePlayer(Player player) {
        if (players.indexOf(player) == -1) {
            return false;
        }
        return true;
    }
    
    public int getID() {
    	return id;
    }

}
