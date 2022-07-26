
package pasaud.voip.protocol.clienttoserver;

public enum Disconnect {
    CLIENTTOKEN(8);

    private final int size;
    Disconnect(int i) {
        size = i;
    }

    public int getSize() {
        return size;
    }
}
