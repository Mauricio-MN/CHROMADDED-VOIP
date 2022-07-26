
package pasaud.voip.Maps;


public class MapsManager {

    private Map[] maps;

/**
* Add manager of maps
     * @param oneChunkRepresentsIntCoords number of coords contains in one chunk;
     * @param mapsCount number of maps;
     * @param chunkSize Count of total chunks( recomended "chunkSize" > 10, "chunkSize" < 30), look "oneChunkRepresentsIntCoords";
*/
    public MapsManager(int oneChunkRepresentsIntCoords, int mapsCount, int chunkSize) {
        maps = new Map[mapsCount];
        for(int i = 0; i < mapsCount; i++){
            maps[i] = new Map(oneChunkRepresentsIntCoords, chunkSize,chunkSize,chunkSize);
        }
    }

    public synchronized Map getMap(int i) {
        return maps[i];
    }

}