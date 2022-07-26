
package pasaud.voip;

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
import pasaud.voip.protocol.clienttoserver.*;

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

                playersManager.addConnect(address, port, name, token, personalNumber);

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

                hashInfo = new HashInfo(address, port, token);
                playersManager.setPosition(hashInfo, map, x, y, z);
                break;
            case AUDIOINFO:
                Stream.of(AudioInfo.values()).forEach(c -> BufferCuts.add(c.getSize()));
                btoken = Arrays.copyOfRange(buffer, 0, BufferCuts.peek() - 1);
                byte[] audio = Arrays.copyOfRange(buffer, BufferCuts.poll(), buffer.length - 1);

                token = new BigInteger(btoken).longValue();

                hashInfo = new HashInfo(address, port, token);
                playersManager.getPlayer(hashInfo).sendAudio(audio);
                break;
            case GROUPINFO:
                Stream.of(GroupInfo.values()).forEach(c -> BufferCuts.add(c.getSize()));
                btoken = Arrays.copyOfRange(buffer, 0, BufferCuts.peek() - 1);
                byte[] bparticipate = Arrays.copyOfRange(buffer, BufferCuts.poll(), BufferCuts.peek() - 1);
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
