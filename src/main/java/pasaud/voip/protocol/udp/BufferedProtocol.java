
package pasaud.voip.protocol.udp;

import java.util.HashMap;

public class BufferedProtocol {

    private HashMap<String, Integer> bufferMap_TypeInt;
    private HashMap<String, Long> bufferMap_TypeLong;
    private HashMap<String, String> bufferMap_TypeString;
    private HashMap<String, Integer> bufferMap_TypeBool;
    private HashMap<String, byte[]> bufferMap_TypeBytes;

    public BufferedProtocol() {
    	bufferMap_TypeInt = new HashMap<>();
    	bufferMap_TypeLong = new HashMap<>();
    	bufferMap_TypeString = new HashMap<>();
    	bufferMap_TypeBool = new HashMap<>();
    	bufferMap_TypeBytes = new HashMap<>();
    }

    public void insertPropierty(String name, byte[] buffer, BufferConvertType type) {
    	
    	switch(type) {
    	  case INTEGER:
    		  Integer outInt = 0;
    		  outInt = new BufferTransform<Integer>().parse(buffer, type, outInt);
    		  bufferMap_TypeInt.put(name, outInt);
    	    break;
    	  case LONG:
    		  Long outLong = 0L;
    		  outLong = new BufferTransform<Long>().parse(buffer, type, outLong);
    		  bufferMap_TypeLong.put(name, outLong);
    		  break;
    	  case STRING:
    		  String outStr = "";
    		  outStr = new BufferTransform<String>().parse(buffer, type, outStr);
    		  bufferMap_TypeString.put(name, outStr);
    	    break;
    	  case BOOL:
    		  
    		  bufferMap_TypeBool.put(name, buffer[0] & 0xFF);
      	    break;
    	  case BYTES:
    		  bufferMap_TypeBytes.put(name, buffer);
      	    break;
    	  default:
    	    
    	}
    }

    
	@SuppressWarnings("unchecked")
	public <T> T getObj(String name, BufferConvertType type, T obj) {
        switch(type) {
	  	  case INTEGER:
	  		if (bufferMap_TypeInt.containsKey(name)) {
	  			obj = (T) bufferMap_TypeInt.get(name);
	        }
	  	    break;
	  	  case LONG:
	  		if (bufferMap_TypeLong.containsKey(name)) {
	  			obj = (T) bufferMap_TypeLong.get(name);
	        }
	  		  break;
	  	  case STRING:
	  		if (bufferMap_TypeString.containsKey(name)) {
	  			obj = (T) bufferMap_TypeString.get(name);
	        }
	  	    break;
	  	  case BOOL:
	  		if (bufferMap_TypeBool.containsKey(name)) {
	  			obj = (T) bufferMap_TypeBool.get(name);
	        }
	    	    break;
	  	  case BYTES:
	  		if (bufferMap_TypeBytes.containsKey(name)) {
	  			obj = (T) bufferMap_TypeBytes.get(name);
	        }
	    	    break;
	  	  default:
	  	    
        }
        
        return obj;
        
    }
}
