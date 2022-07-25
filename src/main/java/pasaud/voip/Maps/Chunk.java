
package pasaud.voip.Maps;

import java.util.LinkedList;
import pasaud.voip.player.PlayerContract;

public class Chunk {

    private LinkedList<PlayerContract> players;

    public Chunk() {
        players = new LinkedList<>();
    }

    public PlayerContract[] getPlayers() {
        PlayerContract[] playersArray = (PlayerContract[]) players.toArray();
        return playersArray;
    }

    public void addPlayer(PlayerContract player) {
        if (players.indexOf(player) == -1) {
            players.add(player);
        }
    }

    public void removePlayer(PlayerContract player) {
        players.removeFirstOccurrence(player);
    }

    public boolean havePlayer(PlayerContract player) {
        if (players.indexOf(player) == -1) {
            return false;
        }
        return true;
    }

}
