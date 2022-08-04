
package pasaud.voip.Maps;

import java.util.LinkedList;
import pasaud.voip.player.PlayerContract;

public class Map {
    private Chunk[][][] chunks;
    private int mapNumb;
    private int oneChunkEqualIntCoordinates;

    private LinkedList<PlayerContract> players;

    public Map(int oneChunkRepresentsIntCoords, int ChunksSizeX, int ChunksSizeY, int ChunksSizeZ) {
        this.oneChunkEqualIntCoordinates = oneChunkRepresentsIntCoords;
        this.chunks = new Chunk[ChunksSizeX][ChunksSizeY][ChunksSizeZ];
        players = new LinkedList<>();
    }

    public synchronized int getChunksSize() {
        return chunks.length;
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

    public synchronized PlayerContract[] getPlayers() {
        if (this.isEmpty()) {
            PlayerContract[] playersArray = (PlayerContract[]) players.toArray();
            return playersArray;
        }
        return null;
    }

    public synchronized boolean isEmpty() {
        return players.isEmpty();
    }

    public synchronized void addPlayer(PlayerContract player) {
        if (players.indexOf(player) == -1) {
            players.add(player);
        }
    }

    public synchronized void removePlayer(PlayerContract player) {
        players.removeFirstOccurrence(player);
    }

    public synchronized boolean havePlayer(PlayerContract player) {
        if (players.indexOf(player) == -1) {
            return false;
        }
        return true;
    }
}
