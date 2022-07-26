
package pasaud.voip;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.math.BigInteger;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.stream.Stream;
import pasaud.voip.protocol.clienttoserver.*;

public class ClientBufferParser implements Runnable {

    private final byte[] buffer;
    private final InetAddress address;
    private final int port;

    public ClientBufferParser(DatagramPacket packet) {
        this.buffer = packet.getData();
        this.address = packet.getAddress();
        this.port = packet.getPort();
    }

    @Override
    public void run() {
        byte[] btoken;
        long token;

        HeaderType headertype = HeaderType.getHeaderType(buffer);
        Queue<Integer> BufferCuts = new LinkedList<>();
        switch (headertype) {
            case HANDCHACKE:
                Stream.of(HandChacke.values()).forEach(c -> BufferCuts.add(c.getSize()));
                btoken = Arrays.copyOfRange(buffer, 0, BufferCuts.peek() - 1);
                byte[] bname = Arrays.copyOfRange(buffer, BufferCuts.poll(), BufferCuts.peek() - 1);
                byte[] bpersonalNumber = Arrays.copyOfRange(buffer, BufferCuts.poll(), BufferCuts.peek() - 1);

                token = new BigInteger(btoken).longValue();
                String name = new String(bname, StandardCharsets.UTF_8);
                int personalNumber = new BigInteger(bpersonalNumber).intValue();

                break;
            case MAPINFO:
                Stream.of(MapInfo.values()).forEach(c -> BufferCuts.add(c.getSize()));
                btoken = Arrays.copyOfRange(buffer, 0, BufferCuts.peek() - 1);
                byte[] bmapId = Arrays.copyOfRange(buffer, BufferCuts.poll(), BufferCuts.peek() - 1);
                byte[] bmapx = Arrays.copyOfRange(buffer, BufferCuts.poll(), BufferCuts.peek() - 1);
                byte[] bmapy = Arrays.copyOfRange(buffer, BufferCuts.poll(), BufferCuts.peek() - 1);
                byte[] bmapz = Arrays.copyOfRange(buffer, BufferCuts.poll(), BufferCuts.peek() - 1);

                token = new BigInteger(btoken).longValue();
                int map = new BigInteger(bmapId).intValue();
                int x = new BigInteger(bmapx).intValue();
                int y = new BigInteger(bmapy).intValue();
                int z = new BigInteger(bmapz).intValue();
                break;
            case AUDIOINFO:
                Stream.of(AudioInfo.values()).forEach(c -> BufferCuts.add(c.getSize()));
                break;
            case GROUPINFO:
                Stream.of(GroupInfo.values()).forEach(c -> BufferCuts.add(c.getSize()));
                break;
            case DISCONNECT:
                Stream.of(Disconnect.values()).forEach(c -> BufferCuts.add(c.getSize()));
                break;
            default:
                Stream.of(HandChacke.values()).forEach(c -> BufferCuts.add(c.getSize()));
                break;
        }

    }

    private void fetchObjectList() {
        for (HandChacke item : HandChacke.values()) {
            int size = item.getSize();
        }
    }
}
