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

public byte[] getBuffer();

public void addBuffer(byte[] buffer);

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


public void sendToGroup();

public void sendToGeral();

public void receiveFromGroup();

public void receiveFromGeral();

}
