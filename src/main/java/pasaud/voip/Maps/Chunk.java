
package pasaud.voip.Maps;

import java.util.LinkedList;
import pasaud.voip.player.PlayerContract;

public class Chunk {

    private LinkedList<PlayerContract> players;

    public Chunk() {
        players = new LinkedList<>();
    }

    public synchronized PlayerContract[] getPlayers() {
        PlayerContract[] playersArray = (PlayerContract[]) players.toArray();
        return playersArray;
    }

    public synchronized void addPlayer(PlayerContract player) {
        if (players.indexOf(player) == -1) {
            players.add(player);
        }
    }

    public synchronized void removePlayer(PlayerContract player) {
        players.removeFirstOccurrence(player);
    }

    public synchronized boolean havePlayer(PlayerContract player) {
        if (players.indexOf(player) == -1) {
            return false;
        }
        return true;
    }

}
