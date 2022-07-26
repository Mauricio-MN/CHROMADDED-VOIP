
package pasaud.voip.protocol.udp.clienttoserver;

public enum AudioInfo {
    CLIENTTOKEN(8), AUDIO(256);

    private final int size;
    AudioInfo(int i) {
        size = i;
    }

    public int getSize() {
        return size;
    }

}
