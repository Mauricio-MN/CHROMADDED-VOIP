
package pasaud.voip;

import java.io.IOException;
import static java.lang.Thread.sleep;
import java.math.BigInteger;
import pasaud.voip.Maps.MapsManager;
import pasaud.voip.player.PlayersManager;

public class Main {

    public static void main(String args[]) {

        try {
            System.out.println("Chromadded Valuable On Inside Projects, ;D");
            System.out.println("Chromadded VOIP?");
            System.out.println("Initializing Maps Manager");
            MapsManager mapsManager = new MapsManager(16, 256, 16);
            System.out.println("Initalized! ");
            System.out.println("Initializing Players Manager");
            PlayersManager playersManager = new PlayersManager(mapsManager);

            byte[] btoken = new byte[]{0,1,2,3,4,5,6,7};
            byte[] bid = new byte[]{0,1,2,3};

            long token = new BigInteger(btoken).longValue();
            int id = new BigInteger(bid).intValue();

            playersManager.addPreConnect(token, id, "Junior", new byte[]{0,2,4,7,2,7,4,9});
            System.out.println("Initalized! ");
            System.out.println("Initializing UDP server");
            Server uspServer = new Server(443, playersManager, mapsManager, DivisionOfThreads.BYMAPS);
            System.out.println("Initalized! ");

            while (true) {
                try {
                    while (true) {
                        Thread.sleep(1000);
                        System.out.println("Receiving UDP packets");
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }

        } catch (IOException ex) {
            System.out.println(ex.toString());
        }


    }

}
