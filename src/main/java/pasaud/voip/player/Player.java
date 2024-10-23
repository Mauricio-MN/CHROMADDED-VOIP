
package pasaud.voip.player;

import pasaud.voip.player.audio.PlayerPacketAudio;
import pasaud.voip.types.Coord;
import pasaud.voip.types.CoordFloat;

import java.net.InetAddress;

/*
 * This implementation need be thread safe
 */
public interface Player {
	
	public void initCrypto(byte[] key);
	
	public byte[] decrypt(byte[] data);
	public byte[] encrypt(byte[] data);
	
	public void setAddress(InetAddress address);
	
	public void setPort(int port);
	
	public InetAddress getAddress();
	
	public int getPort();

	public int getMap();

	public Coord getCoord();
	public CoordFloat getPreciseCoord();

	public String getPublicId();

	public int getID();
	
	public int getScretID();
	
	public int getGroupId();

	public void setMap(int map);

	public void setCoord(float x, float y, float z);
	
	public void setPosition(int map, float x, float y, float z);
	
	public void removeFromMap();

	public void setPublicId(String publicId);

	public void setID(int id);
	
	public void setScretID(int Secret_id);
	
	public void setTalkInLocal(boolean talk);
	public void setTalkInGroup(boolean talk);
	public boolean canTalkInLocal();
	public boolean canTalkInGroup();
	
	public void setListenLocal(boolean talk);
	public void setListenGroup(boolean talk);
	public boolean canListenLocal();
	public boolean canListenGroup();

	public void setGroupId(int id);

	public PlayerState getState();
	
	public void setEnqueueAudioTimeCount(int count);
	public int getEnqueueAudioTimeCount();

	public void setConnectionState(PlayerState state);

	public boolean sendAudioPacketToMe(PlayerPacketAudio packet);
	
	public boolean isBlockedUser(int id);
	
	public void blockUser(Player player);
	
	public void unBlockUser(Player player);
	
	public boolean isValidNumberPacketAudio(int nb);

}
