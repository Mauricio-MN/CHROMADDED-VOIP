
package pasaud.voip.protocol.udp;

import java.util.LinkedHashMap;


public final class ProtocolFromClient {

    public static ProtocolBase AudioInfo;
    public static ProtocolBase Disconnect;
    public static ProtocolBase GroupInfo;
    public static ProtocolBase HandChacke;
    public static ProtocolBase MapInfo;
    
    public final static String ID = "ID";
    public final static String REGISTER_ID = "S_ID";
    public final static String NUMBER = "NUMBER";
    public final static String AUDIO = "AUDIO";
    public final static String MAP = "MAP";
    public final static String X = "X";
    public final static String Y = "Y";
    public final static String Z = "Z";

    public static void init() {
    	
    	LinkedHashMap<String, BufferConvertType> FragmentsTypes = new LinkedHashMap<>();
    	FragmentsTypes.put(ID, BufferConvertType.INTEGER);
    	FragmentsTypes.put(REGISTER_ID, BufferConvertType.INTEGER);
    	FragmentsTypes.put(NUMBER, BufferConvertType.BYTES);
    	FragmentsTypes.put(AUDIO, BufferConvertType.BYTES);
    	FragmentsTypes.put(MAP, BufferConvertType.INTEGER);
    	FragmentsTypes.put(X, BufferConvertType.INTEGER);
    	FragmentsTypes.put(Y, BufferConvertType.INTEGER);
    	FragmentsTypes.put(Z, BufferConvertType.INTEGER);

    	LinkedHashMap<String, Integer> audioFragments = new LinkedHashMap<>();
        audioFragments.put(ID, 4);
        audioFragments.put(NUMBER, 1);
        audioFragments.put(AUDIO, 256 - 9);
        AudioInfo = new ProtocolBase("Audio", audioFragments);
        AudioInfo.registerTypes(FragmentsTypes);

        LinkedHashMap<String, Integer> disconnectFragments = new LinkedHashMap<>();
        disconnectFragments.put(ID, 4);
        Disconnect = new ProtocolBase("Disconnect", disconnectFragments);
        Disconnect.registerTypes(FragmentsTypes);

        LinkedHashMap<String, Integer> groupFragments = new LinkedHashMap<>();
        groupFragments.put(ID, 4);
        groupFragments.put("PARTICIPATE", 1);
        GroupInfo = new ProtocolBase("GroupInfo", groupFragments);
        GroupInfo.registerTypes(FragmentsTypes);

        LinkedHashMap<String, Integer> handchackeFragments = new LinkedHashMap<>();
        handchackeFragments.put(ID, 4);
        
        HandChacke = new ProtocolBase("HandChacke", handchackeFragments);
        HandChacke.registerTypes(FragmentsTypes);

        LinkedHashMap<String, Integer> mapinfoFragments = new LinkedHashMap<>();
        mapinfoFragments.put(ID, 4);
        mapinfoFragments.put(MAP, 4);
        mapinfoFragments.put(X, 4);
        mapinfoFragments.put(Y, 4);
        mapinfoFragments.put(Z, 4);
        MapInfo = new ProtocolBase("MapInfo", mapinfoFragments);
        MapInfo.registerTypes(FragmentsTypes);
        
    }
    
    public static void processBuffer() {
    	
    }

}
