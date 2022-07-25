
package pasaud.voip.group;

import java.util.LinkedList;
import pasaud.voip.player.PlayerContract;

public class Group {

    private int id;
    private LinkedList<PlayerContract> players;

    public Group() {
        players = new LinkedList<>();
    }

    public void resetGroup() {
        players.clear();
    }

    public void addPlayer(PlayerContract player) {
        players.add(player);
    }

    public void removePlayer(PlayerContract player) {
        players.remove(player);
    }

    public LinkedList getPlayers(PlayerContract player) {
        return players;
    }

}
