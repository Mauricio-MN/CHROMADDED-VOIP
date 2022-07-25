
package pasaud.voip.Maps;


public class MapsManager {

    private Map[] maps;

    public MapsManager(int oneChunkRepresentsIntCoords, int mapsCount, int chunkSize) {
        maps = new Map[mapsCount];
        for(int i = 0; i < mapsCount; i++){
            maps[i] = new Map(oneChunkRepresentsIntCoords, chunkSize,chunkSize,chunkSize);
        }
    }

    public Map getMap(int i) {
        return maps[i];
    }

}