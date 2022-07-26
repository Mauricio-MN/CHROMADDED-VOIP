
package pasaud.voip.protocol.clienttoserver;

public enum HeaderType {
    HANDCHACKE(0), MAPINFO(1), AUDIOINFO(2), GROUPINFO(3), DISCONNECT(4);

    private final int headerType;
    HeaderType(int i){
        headerType = i;
    }

    public static HeaderType getHeaderType(byte[] buffer) {
        byte b1 = buffer[0];
        switch (b1) {
            case 0:
                return HeaderType.HANDCHACKE;
            case 1:
                return HeaderType.MAPINFO;
            case 2:
                return HeaderType.AUDIOINFO;
            case 3:
                return HeaderType.GROUPINFO;
            case 4:
                return HeaderType.DISCONNECT;
            default:
                return HeaderType.HANDCHACKE;
        }
    }

}
