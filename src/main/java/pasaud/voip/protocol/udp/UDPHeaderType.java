
package pasaud.voip.protocol.udp;

public enum UDPHeaderType {
    HANDCHACKE(0), AUDIOINFO(1), DISCONNECT(2), POSITION(3);

	private int header;
    UDPHeaderType(int i){
    	header = i;
    }

    public static UDPHeaderType getHeaderType(byte head) {
        
        switch (head) {
            case 0:
                return UDPHeaderType.HANDCHACKE;
            case 1:
                return UDPHeaderType.AUDIOINFO;
            case 2:
                return UDPHeaderType.DISCONNECT;
            default:
                return UDPHeaderType.HANDCHACKE;
        }
    }
    
    public int getHeaderNumber() {
    	return header;
    }

}
