
package pasaud.voip.protocol.udp.clienttoserver;

public enum HandChacke{
    PERSONALNUMBER(4);

    private final int size;
    HandChacke(int i) {
        size = i;
    }

    public int getSize() {
        return size;
    }

}
