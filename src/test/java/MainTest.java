import static org.junit.jupiter.api.Assertions.*;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Arrays;

import org.junit.Assert;
import org.junit.jupiter.api.Test;

import pasaud.voip.maps.ServerMap;
import pasaud.voip.maps.MapsManager;
import pasaud.voip.player.Player;
import pasaud.voip.player.PlayerNormal;
import pasaud.voip.player.PlayersManager;
import pasaud.voip.player.audio.PlayerPacketAudio;
import pasaud.voip.protocol.udp.BufferConvertType;
import pasaud.voip.protocol.udp.BufferTransform;

class MainTest {

	@Test
	void MapsManagerTest() {
		
		ServerMap maps[] = new ServerMap[2];
		maps[0] = new ServerMap(16,16,16,10);
		maps[1] = new ServerMap(16,16,16,10);
		MapsManager.init(maps);
		System.out.println(Arrays.toString(MapsManager.getMaps()));
		Player player = new PlayerNormal();
		player.setMap(1);
		Assert.assertEquals(MapsManager.getMap(1).getPlayers()[0].getID(), player.getID());
	}
	
	@Test
	void BufferTransformTest() {
		Integer id = 0;
		id = new BufferTransform<Integer>().parse(new byte[]{0x01,0x02,0x03,0x04}, BufferConvertType.INTEGER, id);
		Assert.assertEquals((Integer)16909060, id);
	}

}
