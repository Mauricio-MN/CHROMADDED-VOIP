
package pasaud.voip.protocol;

import java.util.HashMap;
import java.util.Map;

public class ProtocolBase {

    private HashMap<String, Integer> fragments;

    private String name;

    public ProtocolBase(String myname, String[] names, int[] sizes) {
        fragments = new HashMap();
        name = myname;

        int i = 0;
        for (String name : names) {
            fragments.put(name, sizes[i]);
            i++;
        }
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
            System.arraycopy(buffer, i, dest, 0, fragment.getValue());
            i += fragment.getValue();
            bprot.insertPropierty(fragment.getKey(), dest);
        }
    }

    public String getName() {
        return name;
    }

}
