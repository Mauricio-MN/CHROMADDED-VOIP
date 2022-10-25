
package pasaud.voip.maps;

import java.util.LinkedList;
import pasaud.voip.player.Player;

public class Map {
    private Chunk[][][] chunks;
    private int oneChunkEqualIntCoordinates;
    private int maxCoordsXY;
    private int maxCoordsZ;
    private int maxAudioDistance;

    private LinkedList<Player> players;

    public Map(int oneChunkRepresentsIntCoords, int ChunksSizeXY, int ChunksSizeZ, int maxAudioDistance) {
        this.maxAudioDistance = maxAudioDistance;
        this.oneChunkEqualIntCoordinates = oneChunkRepresentsIntCoords;
        maxCoordsXY = ChunksSizeXY;
        maxCoordsZ = ChunksSizeZ;
        this.chunks = new Chunk[ChunksSizeXY][ChunksSizeXY][ChunksSizeZ+1];
        players = new LinkedList<>();
    }

    public synchronized int getChunksSize() {
        return chunks.length;
    }

    public synchronized boolean containsChunk(int x, int y, int z){
        if (x > maxCoordsXY || y > maxCoordsXY || z > maxCoordsZ || x < 0 || y < 0 || z < 0) {
            return false;
        }
        return true;
    }
    public synchronized Chunk getChunk(int x, int y, int z) {

        if (chunks[x][y][z] == null) {
            chunks[x][y][z] = new Chunk();
        }
        return chunks[x][y][z];
    }

    public synchronized Chunk getChunkByCoords(int xc, int yc, int zc) {
        int x, y, z = 0;
        x = xc / oneChunkEqualIntCoordinates;
        y = yc / oneChunkEqualIntCoordinates;
        z = zc / oneChunkEqualIntCoordinates;
        if (chunks[x][y][z] == null) {
            chunks[x][y][z] = new Chunk();
        }
        return chunks[x][y][z];
    }

    public synchronized int[] getChunkCoordsByPlayerCoords(int xc, int yc, int zc) {
        int x, y, z = 0;
        x = xc / oneChunkEqualIntCoordinates;
        y = yc / oneChunkEqualIntCoordinates;
        z = zc / oneChunkEqualIntCoordinates;
        if (chunks[x][y][z] == null) {
            chunks[x][y][z] = new Chunk();
        }
        int[] coords = new int[]{x,y,z};
        return coords;
    }

    public synchronized Player[] getPlayers() {
        if (!players.isEmpty()) {
            Player[] playersArray = players.toArray(new Player[0]);
            return playersArray;
        }
        return null;
    }

    public int getMaxAudioDistance() {
        return maxAudioDistance;
    }

    public synchronized boolean isEmpty() {
        return players.isEmpty();
    }

    public synchronized void addPlayer(Player player) {
        if (players.indexOf(player) == -1) {
            players.add(player);
        }
    }

    public synchronized void removePlayer(Player player) {
        players.removeFirstOccurrence(player);
    }

    public synchronized boolean havePlayer(Player player) {
        if (players.indexOf(player) == -1) {
            return false;
        }
        return true;
    }
}
