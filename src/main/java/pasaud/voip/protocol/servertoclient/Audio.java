
package pasaud.voip.protocol.servertoclient;

public enum Audio {
    PUBLICID(4), AUDIO(252);

    private final int size;
    Audio(int i) {
        size = i;
    }

    public int getSize() {
        return size;
    }

}

