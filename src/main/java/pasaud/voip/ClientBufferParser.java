
package pasaud.voip;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.InetAddress;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Stream;
import pasaud.voip.protocol.clienttoserver.*;

public class ClientBufferParser implements Runnable {

    private final byte[] buffer;
    private final int size;
    private final InetAddress address;
    private final int port;

    public ClientBufferParser(byte[] buffer, int size, InetAddress address, int port) {
        this.buffer = buffer;
        this.size = size;
        this.address = address;
        this.port = port;
    }

    @Override
    public void run() {
        HeaderType headertype = HeaderType.getHeaderType(buffer);
        List<String> nameTypes = new LinkedList();
        switch (headertype) {
            case HANDCHACKE:
                Stream.of(HandChacke.values()).forEach(c -> nameTypes.add(c.name()));
                break;
            case MAPINFO:
                Stream.of(MapInfo.values()).forEach(c -> nameTypes.add(c.name()));
                break;
            case AUDIOINFO:
                Stream.of(AudioInfo.values()).forEach(c -> nameTypes.add(c.name()));
                break;
            case GROUPINFO:
                Stream.of(GroupInfo.values()).forEach(c -> nameTypes.add(c.name()));
                break;
            case DISCONNECT:
                Stream.of(Disconnect.values()).forEach(c -> nameTypes.add(c.name()));
                break;
            default:
                Stream.of(HandChacke.values()).forEach(c -> nameTypes.add(c.name()));
                break;
        }

    }

    private void fetchObjectList() {
        for (HandChacke item : HandChacke.values()) {
            int size = item.getSize();
        }
    }
}
