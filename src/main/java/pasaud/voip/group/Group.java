
package pasaud.voip.group;

import java.util.LinkedList;
import pasaud.voip.player.Player;

public class Group {

    private int id;
    private LinkedList<Player> players;

    public Group() {
        players = new LinkedList<>();
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

    public LinkedList getPlayers(Player player) {
        return players;
    }

}
