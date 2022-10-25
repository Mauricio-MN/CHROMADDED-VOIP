
package pasaud.voip.maps;


public final class MapsManager {

    private static Map[] maps;
    
    private MapsManager() {
    	
    }

    /**
     * Add manager of maps
     *
     * @param Maps Map[], list of maps;
     */
    public static void init(Map[] Maps) {
            maps = Maps;
    }

    public static Map getMap(int i) {
    	if(i < 0 || i > maps.length - 1) {
    		return null;
    	}
        return maps[i];
    }
    
    public static int getSize() {
    	return maps.length;
    }

    public synchronized static Map[] getMaps() {
        return maps;
    }

}