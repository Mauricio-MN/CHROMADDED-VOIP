
package pasaud.voip.protocol.clienttoserver;

public enum GroupInfo {
    CLIENTTOKEN(8), PARTICIPATE(1);

    private final int size;
    GroupInfo(int i) {
        size = i;
    }

    public int getSize() {
        return size;
    }

}
