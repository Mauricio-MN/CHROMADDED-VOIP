
package pasaud.voip.player;

import pasaud.voip.player.audio.PlayerPacketAudio;
import pasaud.voip.player.hash.PlayerHashInfo;
import pasaud.voip.player.audio.PlayerAudioType;

public interface Player {

	public int getMap();

	public int getXcoord();

	public int getYcoord();

	public int getZcoord();

	public String getName();

	public int getID();

	public boolean getIsGroupTalk();

	public void setHashCode(PlayerHashInfo hash);

	public PlayerHashInfo getHashCode();
	
	public void setHashCodePreConnect(PlayerHashInfo hash);

	public PlayerHashInfo getHashCodePreConnect();

	public void setMap(int map);

	public void setXcoord(int x);

	public void setYcoord(int y);

	public void setZcoord(int z);
	
	public void setPosition(int map, int x, int y, int z);

	public void setName(String name);

	public void setID(int id);

	public void setGroupTalking(boolean isTalking);

	public void setGroup(int id);

	public PlayerState getState();

	public void setConnectionState(PlayerState state);

	/**
	 * Add Audio Buffer to Player Queue;
	 * 
	 * @param audioType PlayerAudioType ENUM;
	 * @param audio     Buffer;
	 * @return PlayerPacketAudio;
	 */
	public PlayerPacketAudio packetMyAudio(PlayerAudioType audioType, Integer number, byte[] audio);

	public boolean sendPacketToMe(PlayerAudioType audioType, PlayerPacketAudio packet);
	
	public boolean isValidNumberPacketAudio(int nb);

}
