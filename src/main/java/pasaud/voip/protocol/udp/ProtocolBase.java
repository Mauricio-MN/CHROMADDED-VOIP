
package pasaud.voip.protocol.udp;

import java.util.LinkedHashMap;
import java.util.Map;

public class ProtocolBase {

    private LinkedHashMap<String, Integer> fragments;
    private LinkedHashMap<String, BufferConvertType> fragments_types;

    private String name;

    public ProtocolBase(String myname, LinkedHashMap<String, Integer> fragments) {
        this.fragments = fragments;
        this.name = myname;
        
    }
    
    public void registerTypes(LinkedHashMap<String, BufferConvertType> fragments_types) {
    	this.fragments_types = fragments_types;
    }
 
    public int getSize(String name) {
        if (fragments.containsKey(name)) {
            return fragments.get(name);
        }
        return 0;
    }

    /**
     * Process buffer to a DTO info object
     * @param buffer buffer bytes;
     * @param bprot DTO reference;
     * @return return extra information in buffer
     */
    public byte[] bufferProcess(byte[] buffer, BufferedProtocol bprot){
        int i = 0;
        
        for (Map.Entry<String, Integer> fragment : fragments.entrySet()) {
            byte[] dest = new byte[fragment.getValue()];
            if (buffer.length - i < fragment.getValue()){
                System.arraycopy(buffer, i, dest, 0, buffer.length - i);
            } else {
                System.arraycopy(buffer, i, dest, 0, fragment.getValue());
            }
            i += fragment.getValue();
            
            bprot.insertProperty(fragment.getKey(), dest, fragments_types.get(fragment.getKey()));
        }
        
        if (buffer.length > i) {
            byte[] extra = new byte[buffer.length - i];
            System.arraycopy(buffer, i, extra, 0, buffer.length - i);
            return extra;
        }
        
        return new byte[0];
    }

    public String getName() {
        return name;
    }

}
