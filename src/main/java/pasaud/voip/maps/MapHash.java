
package pasaud.voip.maps;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

import pasaud.voip.player.Player;
import pasaud.voip.types.Coord;

public class MapHash {
    private int oneChunkEqualIntCoordinates;
    private int maxCoordsXY;
    private int maxCoordsZ;
    private int maxAudioDistance;
    
    ConcurrentHashMap<String, ConcurrentLinkedQueue<Player>> chunks;

    private ConcurrentHashMap<Integer, Player> players;
    
    public MapHash(int oneChunkRepresentsIntCoords, int ChunksSizeXY, int ChunksSizeZ, int maxAudioDistance) {
		this.maxAudioDistance = maxAudioDistance;
	    this.oneChunkEqualIntCoordinates = oneChunkRepresentsIntCoords;
	    maxCoordsXY = ChunksSizeXY;
	    maxCoordsZ = ChunksSizeZ;
        this.chunks = new ConcurrentHashMap<String, ConcurrentLinkedQueue<Player>>();
    }

    public synchronized int getChunksSize() {
        return maxCoordsXY;
    }

    public boolean containsChunk(Coord coord){
    	int x = coord.getX();
    	int y = coord.getY();
    	int z = coord.getZ();
        if (x > maxCoordsXY || y > maxCoordsXY || z > maxCoordsZ || x < 0 || y < 0 || z < 0) {
            return false;
        }
        return true;
    }

    public Coord getChunkCoordsByPlayerCoords(Coord coord) {
    	int xc = coord.getX();
    	int yc = coord.getY();
    	int zc = coord.getZ();
        int x, y, z = 0;
        x = xc / oneChunkEqualIntCoordinates;
        y = yc / oneChunkEqualIntCoordinates;
        z = zc / oneChunkEqualIntCoordinates;
        Coord coords = new Coord(x,y,z);
        return coords;
    }

    public Player[] getPlayers() {
        if (!players.isEmpty()) {
            Player[] playersArray = players.values().toArray(new Player[0]);
            return playersArray;
        }
        Player[] playersArray = new Player[0];
        return playersArray;
    }

    public int getMaxAudioDistance() {
        return maxAudioDistance;
    }

    public boolean isEmpty() {
        return players.isEmpty();
    }
    
    private void checkChunk(String chunkHash) {
    	if(!chunks.containsKey(chunkHash)) {
    		chunks.put(chunkHash, new ConcurrentLinkedQueue<Player>());
        }
    }
    
    private boolean chunkExist(String chunkHash) {
    	if(!chunks.containsKey(chunkHash)) {
    		return false;
        }
    	return true;
    }

    public void addPlayer(Player player) {
        if (!players.containsKey(player.getID())) {
            players.put(player.getID(), player);
            
            Coord chunkCoord = getChunkCoordsByPlayerCoords(player.getCoord());
            int x = chunkCoord.getX();
            int y = chunkCoord.getX();
            int z = chunkCoord.getX();
            
            String chunkHash = ""+x+""+y+""+z;
            checkChunk(chunkHash);
            chunks.get(chunkHash).add(player);
            
        }
    }
    
    public void removeFromCoord(Player player, Coord lastCoord) {
    	if (players.containsKey(player.getID())) {
            
            Coord chunkCoord = getChunkCoordsByPlayerCoords(lastCoord);
            int x = chunkCoord.getX();
            int y = chunkCoord.getX();
            int z = chunkCoord.getX();
            String chunkHash = ""+x+""+y+""+z;
            if(chunkExist(chunkHash)) {
            	chunks.get(chunkHash).remove(player);
            }
        }
    }
    
    public void addInCoord(Player player, Coord newCoord) {
    	if (players.containsKey(player.getID())) {
            
            Coord chunkCoord = getChunkCoordsByPlayerCoords(newCoord);
            int x = chunkCoord.getX();
            int y = chunkCoord.getX();
            int z = chunkCoord.getX();
            String chunkHash = ""+x+""+y+""+z;
            
            if(chunkExist(chunkHash)) {
            	chunks.get(chunkHash).add(player);
            }
        }
    }

    public void removePlayer(int id) {
    	if(players.containsKey(id)) {
    		Player player = players.get(id);
    		removeFromCoord(player, player.getCoord());
    		players.remove(id);
    	}
    }

    public boolean havePlayer(int id) {
        if (players.containsKey(id)) {
            return true;
        }
        return false;
    }
}
