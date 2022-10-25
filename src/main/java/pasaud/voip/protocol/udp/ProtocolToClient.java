
package pasaud.voip.protocol.udp;

import java.nio.ByteBuffer;
import java.util.LinkedHashMap;


public final class ProtocolToClient {

    public static ProtocolBase AudioInfo;
    public static ProtocolBase Disconnect;
    public static ProtocolBase HandChacke;
    
    public final static String ID = "ID";
    public final static String REGISTER_ID = "S_ID";
    public final static String NUMBER = "NUMBER";
    public final static String AUDIO = "AUDIO";
    public final static String MAP = "MAP";
    public final static String X = "X";
    public final static String Y = "Y";
    public final static String Z = "Z";

    public static void init() {

    	LinkedHashMap<String, Integer> audioFragments = new LinkedHashMap<>();
        audioFragments.put(ID, 4);
        audioFragments.put(NUMBER, 1);
        audioFragments.put(AUDIO, 256 - 5);
        AudioInfo = new ProtocolBase("Audio", audioFragments);

        LinkedHashMap<String, Integer> disconnectFragments = new LinkedHashMap<>();
        disconnectFragments.put(ID, 4);
        Disconnect = new ProtocolBase("Disconnect", disconnectFragments);

        LinkedHashMap<String, Integer> handchackeFragments = new LinkedHashMap<>();
        handchackeFragments.put(ID, 4);
        HandChacke = new ProtocolBase("HandChacke", handchackeFragments);
        
    }
    
    public static byte[] createAudioInfo(Integer id, Integer number, byte[] audio) {
    	int idSize = AudioInfo.getSize(ID);
    	int numberSize = AudioInfo.getSize(NUMBER);
    	
    	byte[] out = new byte[1 + audio.length + idSize + numberSize];
    	Integer first_pos = 0;
    	Integer id_pos = 1;
    	Integer number_pos = id_pos + AudioInfo.getSize(ID);
    	Integer audio_pos = number_pos + AudioInfo.getSize(NUMBER);
    	 
    	byte[] header = {(byte) UDPHeaderType.AUDIOINFO.getHeaderNumber()};
    	byte[] idByte = ByteBuffer.allocate(idSize).putInt(id).array();
    	byte[] numberByte = ByteBuffer.allocate(numberSize).putInt(number).array();
    	
    	System.arraycopy(header, 0, out, first_pos, header.length);
    	System.arraycopy(idByte, 0, out, id_pos, idByte.length);
    	System.arraycopy(numberByte, 0, number_pos, first_pos, numberByte.length);
    	System.arraycopy(audio, 0, out, audio_pos, audio.length);
 
    	return out;
    }
    
    

}
