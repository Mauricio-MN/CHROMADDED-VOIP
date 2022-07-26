/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package pasaud.voip.player;

/**
 *
 * @author wghat
 */
public interface PlayerContract {
 
public int getMap();
public int getXcoord();
public int getYcoord();
public int getZcoord();

public String getName();
public int getID();
public long getHashMapNb();

public boolean getIsGroupTalk();

public void setMap(int map);
public void setXcoord(int x);
public void setYcoord(int y);
public void setZcoord(int z);

public void setName(String name);
public void setID(int id);
public void setHashMapNb(long numberMap);

public void setGroup(int id);

public PlayerState getState();
public void setConnectionState(PlayerState state);

/**
* Add Audio Buffer to Player Queue to send to Group of players;
     * @param audio Buffer
*/
public void sendToGroup(byte[] audio);

/**
* Add Audio Buffer to Player Queue to send to Geral(in map Location);
     * @param audio Buffer
*/
public void sendToGeral(byte[] audio);

/**
* Add Audio Buffer to Player from another player of Group;
     * @param packet PlayerPacketAudio class, packet Player info + audio Buffer
*/
public void receiveFromGroup(PlayerPacketAudio packet);

/**
* Add Audio Buffer to Player from another player of Geral(from Map location);
     * @param packet
*/
public void receiveFromGeral(PlayerPacketAudio packet);

/**
* get Packet audio buffer from player;
*/
 public PlayerPacketAudio udpBufferQueueClean();
}
