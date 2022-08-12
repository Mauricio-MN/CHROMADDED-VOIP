
package pasaud.voip.Maps;

import java.util.LinkedList;
import pasaud.voip.player.Player;

public class Chunk {

    private LinkedList<Player> players;

    public Chunk() {
        players = new LinkedList<>();
    }

    public synchronized Player[] getPlayers() {
        Player[] playersArray = (Player[]) players.toArray();
        return playersArray;
    }

    public synchronized void addPlayer(Player player) {
        if (players.indexOf(player) == -1) {
            players.add(player);
        }
    }

    public synchronized void removePlayer(Player player) {
        players.removeFirstOccurrence(player);
    }

    public synchronized boolean havePlayer(Player player) {
        if (players.indexOf(player) == -1) {
            return false;
        }
        return true;
    }

}
