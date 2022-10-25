
package pasaud.voip.protocol.udp;

import java.io.UnsupportedEncodingException;

public class BufferTransform<T> {

    @SuppressWarnings("unchecked")
	public T parse(byte[] buffer, BufferConvertType type, T ref) {
        switch (type) {
            case LONG:
            	ref = intlongConvert(buffer, ref);
                break;
            case INTEGER:
            	ref = intlongConvert(buffer, ref);
                break;
            case STRING:
			try {
				ref = (T)new String(buffer, "UTF-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
				ref = (T)"NaN";
			};
				break;
            default:
        }
        return ref;
    }

    
    
	@SuppressWarnings("unchecked")
	private T intlongConvert(byte[] buffer, T ref) {
        Long parse = 0L;
        for (byte b : buffer) {
            parse = (parse << 8) + (b & 0xFF);
        }
        Integer intParse = parse.intValue();
        Long longParse = parse.longValue();

        if(ref.getClass().getSimpleName().equals(parse.getClass().getSimpleName())){
            ref = (T)longParse;
        } else {
            ref = (T)intParse;
        }

        return ref;
    }

}
