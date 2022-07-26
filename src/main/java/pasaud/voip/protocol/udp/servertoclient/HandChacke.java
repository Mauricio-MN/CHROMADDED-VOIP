
package pasaud.voip.protocol.udp.servertoclient;

public enum HandChacke {
    CLIENTTOKEN(8), CLIENTNAME(20), PERSONALNUMBER(8);

    private final int size;
    HandChacke(int i) {
        size = i;
    }

    public int getSize() {
        return size;
    }

}