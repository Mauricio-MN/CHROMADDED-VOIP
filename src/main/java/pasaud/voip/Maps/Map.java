
package pasaud.voip.Maps;

public class Map {
    private Chunk[][][] chunks;
    private int mapNumb;
    private int oneChunkEqualIntCoordinates;

    public Map(int oneChunkRepresentsIntCoords, int ChunksSizeX, int ChunksSizeY, int ChunksSizeZ) {
        this.oneChunkEqualIntCoordinates = oneChunkRepresentsIntCoords;
        this.chunks = new Chunk[ChunksSizeX][ChunksSizeY][ChunksSizeZ];
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
}
