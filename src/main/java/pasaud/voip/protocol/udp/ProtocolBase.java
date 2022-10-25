
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

    public void bufferProcess(byte[] buffer, BufferedProtocol bprot){
        int i = 0;
        for (Map.Entry<String, Integer> fragment : fragments.entrySet()) {
            byte[] dest = new byte[fragment.getValue()];
            if (buffer.length - i < fragment.getValue()){
                System.arraycopy(buffer, i, dest, 0, buffer.length - i);
            } else {
                System.arraycopy(buffer, i, dest, 0, fragment.getValue());
            }
            i += fragment.getValue();
            
            bprot.insertPropierty(fragment.getKey(), dest, fragments_types.get(fragment.getKey()));
        }
    }

    public String getName() {
        return name;
    }

}
