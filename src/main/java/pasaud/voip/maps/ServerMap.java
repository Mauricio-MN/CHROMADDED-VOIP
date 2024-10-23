package pasaud.voip.maps;

import java.util.concurrent.ConcurrentHashMap;

import pasaud.voip.player.Player;
import pasaud.voip.types.Coord;

public class ServerMap {
    private Chunk[][][] chunks;
    private int oneChunkEqualIntCoordinates;
    private int maxCoordsXY;
    private int maxCoordsZ;
    private int maxAudioDistance;

    private ConcurrentHashMap<Integer, Player> players;
    
    public ServerMap() {
    	this.maxAudioDistance = 0;
        this.oneChunkEqualIntCoordinates = 1;
    	maxCoordsXY = 1;
        maxCoordsZ = 1;
        this.chunks = new Chunk[1][1][1];
    }

    public ServerMap(int oneChunkRepresentsIntCoords, int ChunksSizeXY, int ChunksSizeZ, int maxAudioDistance) {
        this.maxAudioDistance = maxAudioDistance;
        this.oneChunkEqualIntCoordinates = oneChunkRepresentsIntCoords;
        maxCoordsXY = ChunksSizeXY;
        maxCoordsZ = ChunksSizeZ;
        this.chunks = new Chunk[ChunksSizeXY][ChunksSizeXY][ChunksSizeZ+1];
        for (int x = 0; x < ChunksSizeXY; x++) {
        	for (int y = 0; y < ChunksSizeXY; y++) {
        		for (int z = 0; z < ChunksSizeZ+1; z++) {
        			this.chunks[x][y][z] = new Chunk();
                }
            }
        }
        players = new ConcurrentHashMap<>();
    }

    public synchronized int getChunksSize() {
        return chunks.length;
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
    public Chunk getChunk(Coord coord) {
    	int x = coord.getX();
    	int y = coord.getY();
    	int z = coord.getZ();
        if (chunks[x][y][z] == null) {
            chunks[x][y][z] = new Chunk();
        }
        return chunks[x][y][z];
    }

    public Chunk getChunkByPlayerCoords(Coord coord) {
    	Coord chunkCoord = getChunkCoordsByPlayerCoords(coord);
        int x = chunkCoord.getX();
        int y = chunkCoord.getY();
        int z = chunkCoord.getZ();
        
        if (chunks[x][y][z] == null) {
            return new Chunk();
        }
        
        return chunks[x][y][z];
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

    public void addPlayer(Player player) {
        if (!players.containsKey(player.getID())) {
            players.put(player.getID(), player);
        }
    }

    public void removePlayer(int id) {
    	if(players.containsKey(id)) {
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
