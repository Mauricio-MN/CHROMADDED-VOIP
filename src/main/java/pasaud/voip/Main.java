
package pasaud.voip;

import java.io.IOException;
import pasaud.voip.Maps.MapsManager;
import pasaud.voip.player.PlayersManager;

public class Main {

    public static void main(String args[]) {

        try {
            MapsManager mapsManager = new MapsManager(16, 256, 16);
            PlayersManager playersManager = new PlayersManager(mapsManager);
            Server uspServer = new Server(443, playersManager);

        } catch (IOException ex) {
            System.out.println(ex.toString());
        }


    }

}
