
package pasaud.voip;

import pasaud.voip.protocol.udp.clienttoserver.GroupInfo;
import pasaud.voip.protocol.udp.clienttoserver.MapInfo;
import pasaud.voip.protocol.udp.clienttoserver.HeaderType;
import pasaud.voip.protocol.udp.clienttoserver.AudioInfo;
import pasaud.voip.protocol.udp.clienttoserver.Disconnect;
import pasaud.voip.protocol.udp.clienttoserver.HandChacke;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.math.BigInteger;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;
import java.util.stream.Stream;
import pasaud.voip.player.HashInfo;
import pasaud.voip.player.PlayersManager;

public class ClientBufferParser implements Runnable {

    private final byte[] buffer;
    private final InetAddress address;
    private final int port;
    private PlayersManager playersManager;
    private DatagramSocket socket;

    public ClientBufferParser(DatagramSocket socket, DatagramPacket packet, PlayersManager playersManager) {
        this.buffer = packet.getData();
        this.address = packet.getAddress();
        this.port = packet.getPort();

        this.playersManager = playersManager;
        this.socket = socket;
    }

    @Override
    public void run() {
        byte[] btoken;
        long token;
        HashInfo hashInfo;

        int i = 0;

        HeaderType headertype = HeaderType.getHeaderType(buffer);
        Queue<Integer> BufferCuts = new LinkedList<>();
        switch (headertype) {
            case HANDCHACKE:
                for (HandChacke protocol : HandChacke.values()) {
                    BufferCuts.add(protocol.getSize() + i);
                    i += protocol.getSize();
                }
                i = 0;
                System.out.println(buffer.length + " " + (BufferCuts.peek() + 1) + "falta 1");
                byte[] bpersonalNumber = Arrays.copyOfRange(buffer, 1, BufferCuts.poll());

                int personalNumber = new BigInteger(bpersonalNumber).intValue();

                playersManager.addConnect(address, port, personalNumber);

                break;
            case MAPINFO:
                for (MapInfo protocol : MapInfo.values()) {
                    BufferCuts.add(protocol.getSize() + i);
                    i += protocol.getSize();
                }
                i = 0;
                btoken = Arrays.copyOfRange(buffer, 1, BufferCuts.peek());
                byte[] bmapId = Arrays.copyOfRange(buffer, BufferCuts.poll()+1, BufferCuts.peek());
                byte[] bmapx = Arrays.copyOfRange(buffer, BufferCuts.poll()+1, BufferCuts.peek());
                byte[] bmapy = Arrays.copyOfRange(buffer, BufferCuts.poll()+1, BufferCuts.peek());
                byte[] bmapz = Arrays.copyOfRange(buffer, BufferCuts.poll()+1, BufferCuts.peek());

                token = new BigInteger(btoken).longValue();
                int map = new BigInteger(bmapId).intValue();
                int x = new BigInteger(bmapx).intValue();
                int y = new BigInteger(bmapy).intValue();
                int z = new BigInteger(bmapz).intValue();

                hashInfo = new HashInfo(address, port, token);
                playersManager.setPosition(hashInfo, map, x, y, z);
                break;
            case AUDIOINFO:
                for (AudioInfo protocol : AudioInfo.values()) {
                    BufferCuts.add(protocol.getSize() + i);
                    i += protocol.getSize();
                }
                i = 0;
                btoken = Arrays.copyOfRange(buffer, 1, BufferCuts.peek());
                byte[] audio = Arrays.copyOfRange(buffer, BufferCuts.poll()+1, buffer.length - 1);

                token = new BigInteger(btoken).longValue();

                hashInfo = new HashInfo(address, port, token);
                playersManager.getPlayer(hashInfo).queueMyPacket(audio);
                break;
            case GROUPINFO:
                Stream.of(GroupInfo.values()).forEach(c -> BufferCuts.add(c.getSize()));
                btoken = Arrays.copyOfRange(buffer, 1, BufferCuts.peek());
                byte[] bparticipate = Arrays.copyOfRange(buffer, BufferCuts.poll()+1, BufferCuts.peek());
                boolean participate = bparticipate[0] == 1;

                token = new BigInteger(btoken).longValue();

                hashInfo = new HashInfo(address, port, token);
                playersManager.getPlayer(hashInfo).setGroupTalking(participate);
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
