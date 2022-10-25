
package pasaud.voip;

import java.net.DatagramPacket;
import java.net.InetAddress;

import pasaud.voip.player.PlayersManager;
import pasaud.voip.player.audio.PlayerAudioType;
import pasaud.voip.player.audio.PlayerPacketAudio;
import pasaud.voip.player.hash.PlayerConnectHashInfo;
import pasaud.voip.player.hash.PlayerHashInfo;
import pasaud.voip.protocol.udp.BufferConvertType;
import pasaud.voip.protocol.udp.BufferedProtocol;
import pasaud.voip.protocol.udp.ProtocolFromClient;
import pasaud.voip.protocol.udp.UDPHeaderType;

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
        Integer id = -1;
        Integer reg_id = -1;
        PlayerHashInfo hashInfo;
        BufferedProtocol bufferedProtocol = new BufferedProtocol();


        UDPHeaderType headertype = UDPHeaderType.getHeaderType(buffer[0]);
        
        
        byte[] fragmentsBuffer = new byte[buffer.length - 1];
        System.arraycopy(buffer, 1, fragmentsBuffer, 0, fragmentsBuffer.length);
        
        switch (headertype) {
            case HANDCHACKE:
                
                ProtocolFromClient.HandChacke.bufferProcess(fragmentsBuffer, bufferedProtocol);

                reg_id = bufferedProtocol.getObj(ProtocolFromClient.REGISTER_ID, BufferConvertType.INTEGER, reg_id);
                id = bufferedProtocol.getObj(ProtocolFromClient.ID, BufferConvertType.INTEGER, id);
                
                PlayerHashInfo hashinfo = new PlayerConnectHashInfo(reg_id, id, address, port);
                
                PlayersManager.addConnect(hashinfo);

                break;
            case POSITION:
            	
                ProtocolFromClient.MapInfo.bufferProcess(fragmentsBuffer, bufferedProtocol);
                
                
                id = bufferedProtocol.getObj(ProtocolFromClient.ID, BufferConvertType.INTEGER, id);
      
                Integer map = 0; Integer x = 0; Integer y = 0; Integer z = 0;
                map = bufferedProtocol.getObj(ProtocolFromClient.MAP, BufferConvertType.INTEGER, map);
                
                x = bufferedProtocol.getObj(ProtocolFromClient.X, BufferConvertType.INTEGER, x);
                y = bufferedProtocol.getObj(ProtocolFromClient.Y, BufferConvertType.INTEGER, y);
                z = bufferedProtocol.getObj(ProtocolFromClient.Z, BufferConvertType.INTEGER, z);
 
                hashInfo = new PlayerConnectHashInfo(-1, id, address, port);
                
                PlayersManager.getPlayer(hashInfo).setPosition(map, x, y, z);
                break;
            case AUDIOINFO:
            	
            	ProtocolFromClient.AudioInfo.bufferProcess(fragmentsBuffer, bufferedProtocol);

            	id = bufferedProtocol.getObj(ProtocolFromClient.ID, BufferConvertType.INTEGER, id);
                byte[] number = new byte[1];
                byte[] audio = new byte[1];
                number = bufferedProtocol.getObj(ProtocolFromClient.NUMBER, BufferConvertType.BYTES, number);
                audio = bufferedProtocol.getObj(ProtocolFromClient.AUDIO, BufferConvertType.BYTES, audio);
                
                hashInfo = new PlayerConnectHashInfo(-1, id, address, port);
                
                int numberAudio = Byte.toUnsignedInt(number[0]);
                if (PlayersManager.containsPlayerConnected(hashInfo)) {
                    PlayerPacketAudio packet = PlayersManager.getPlayer(hashInfo).packetMyAudio(PlayerAudioType.TOGERAL, numberAudio, audio);

                    new Thread(new AudioPropage(packet)).start();
                }
                break;
            case DISCONNECT:
            	
                break;
            default:
                break;
        }

    }

}
