
package pasaud.voip.protocol.clienttoserver;

public enum MapInfo {
    CLIENTTOKEN(8), MAPID(4), MAPX(4), MAPY(4), MAPZ(4);

    private final int size;
    MapInfo(int i) {
        size = i;
    }

    public int getSize() {
        return size;
    }
}
