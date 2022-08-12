
package pasaud.voip.protocol;

import java.util.HashMap;

public class BufferedProtocol {

    private HashMap<String, byte[]> bufferMap;

    public BufferedProtocol() {
        bufferMap = new HashMap();
    }

    public void insertPropierty(String name, byte[] buffer) {
        bufferMap.put(name, buffer);
    }
}
