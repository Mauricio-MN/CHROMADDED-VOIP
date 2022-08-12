/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package pasaud.voip.player;

import pasaud.voip.player.audio.PlayerPacketAudio;
import pasaud.voip.player.audio.PlayerAudioType;

/**
 *
 * @author wghat
 */
public interface Player {

public int getMap();
public int getXcoord();
public int getYcoord();
public int getZcoord();

public String getName();
public int getID();
public long getHashMapNb();

public boolean getIsGroupTalk();

public void setKey(HashInfo key);
public HashInfo getKey();

public void setMap(int map);
public void setXcoord(int x);
public void setYcoord(int y);
public void setZcoord(int z);

public void setName(String name);
public void setID(int id);
public void setHashMapNb(long numberMap);

public void setGroupTalking(boolean isTalking);
public void setGroup(int id);

public PlayerState getState();
public void setConnectionState(PlayerState state);

/**
* Add Audio Buffer to Player Queue;
     * @param audioType PlayerAudioType ENUM;
     * @param audio Buffer;
     * @return PlayerPacketAudio;
*/
public PlayerPacketAudio queueMyPacket(PlayerAudioType audioType, byte[] audio);

/**
* Add Audio Buffer to Player from another player of Geral(from Map location);
     * @param packet
*/
public void addAudioToQueue(PlayerPacketAudio packet);

/**
* get Packet audio buffer from player;
     * @return packetaudio
*/
 public PlayerPacketAudio getPacket();
}
