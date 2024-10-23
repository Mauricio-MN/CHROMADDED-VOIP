
package pasaud.voip.maps;


public final class MapsManager {

    private static ServerMap[] maps;
    
    private MapsManager() {
    	
    }

    /**
     * Add manager of maps
     *
     * @param Maps Map[], list of maps;
     */
    public static void init(ServerMap[] Maps) {
            maps = Maps;
    }
    
    public static boolean existMap(int i) {
    	if(i < 0 || i >= maps.length) {
    		return false;
    	}
        return true;
    }

    public static ServerMap getMap(int i) {
    	if(i < 0 || i >= maps.length) {
    		return new ServerMap();
    	}
        return maps[i];
    }
    
    public static int getSize() {
    	return maps.length;
    }

    public synchronized static ServerMap[] getMaps() {
        return maps;
    }

}