package pasaud.voip.player;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;

public class Player implements PlayerContract{

private int x;

private int y;

private int z;

private int map;

private int numberMapHash;

private int id;

private String name;

private int groupId;

private PlayerState state;

private Queue<byte[]> buffer = new LinkedList<>();

Player(){

this.x = -1;
this.y = -1;
this.z = -1;
this.map = -1;
this.numberMapHash = -1;
this.id = -1;
this.name = "";
this.state = PlayerState.EMPTY;
this.groupId = -1;


}



@Override
public int getXcoord(){
return x;
}

@Override
public int getYcoord(){
return y;
}

@Override
public int getZcoord(){
return z;
}

@Override
public String getName(){
return name;
}

@Override
public int getID(){
return id;
}

@Override
public int getHashMapNb(){
return numberMapHash;
}

@Override
public boolean getIsGroupTalk(){
return groupId != -1;
}

@Override
public byte[] getBuffer(){
return buffer.poll();
}

@Override
public void addBuffer(byte[] buffer){

byte[] copyBuffer = Arrays.copyOf(buffer, buffer.length);

this.buffer.add(copyBuffer);
}

@Override
public void setXcoord(int x){
this.x = x;
}

@Override
public void setYcoord(int y){
this.y = y;
}

@Override
public void setZcoord(int z){
this.z = z;
}

@Override
public void setName(String name){
this.name = name;
}

@Override
public void setID(int id){
this.id = id;
}

@Override
public void setHashMapNb(int numberMapHash){
this.numberMapHash = numberMapHash;
}

@Override
public PlayerState getState(){
return state;
}

@Override
public void setState(PlayerState state){
this.state = state;
}
    
}
