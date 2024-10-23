package pasaud.voip.player;

import pasaud.voip.player.audio.PlayerPacketAudio;
import pasaud.voip.protocol.Protocol.Server;
import pasaud.voip.rooms.Room;
import pasaud.voip.rooms.RoomsManager;
import pasaud.voip.types.CAddress;
import pasaud.voip.types.Coord;
import pasaud.voip.types.CoordFloat;

import java.net.InetAddress;
import java.time.Instant;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import com.google.protobuf.ByteString;

import pasaud.voip.UDPServer;
import pasaud.voip.crypto.Crypto;
import pasaud.voip.genericsocket.SendDatagram;
import pasaud.voip.maps.MapsManager;

public class PlayerNormal implements Player {

    private Coord coord;
    private CoordFloat preciseCoord;
    
    private Object setMapSync;
    private Object setCoord;

    private AtomicInteger map;

    private AtomicInteger id;
    
    private AtomicInteger secret_id;

    private String publicId;

    private AtomicInteger groupId;
    
    private AtomicBoolean needSendCoordPos;

    private PlayerState connectionState;
    
    private Crypto crypto;
    private AtomicBoolean cryptoIsActive;
    
    private CAddress caddress;
    
    SendDatagram sender;
    
    private static ConcurrentHashMap<Integer, Player> blockedUsers;
    private static ConcurrentHashMap<Integer, ConcurrentLinkedQueue<PlayerPacketAudio>> audioPacketQueue;
    
    private AtomicInteger audioPacketNeededTime;
    private Instant lastAudioSendTime;
    private Object lastAudioSendTimeSync;
    
    private AtomicInteger packetNumber;
    private AtomicBoolean localTalk;
    private AtomicBoolean groupTalk;
    private AtomicBoolean localListen;
    private AtomicBoolean groupListen;

    public PlayerNormal() {
    	blockedUsers = new ConcurrentHashMap<>();
    	this.id = new AtomicInteger();
    	this.secret_id = new AtomicInteger();
    	this.map = new AtomicInteger();
    	this.cryptoIsActive = new AtomicBoolean();
    	this.needSendCoordPos = new AtomicBoolean();
        this.groupId = new AtomicInteger();
        this.audioPacketQueue = new ConcurrentHashMap<>();
        this.audioPacketNeededTime = new AtomicInteger();
        this.audioPacketNeededTime.set(0);
        this.lastAudioSendTime = Instant.now();
        this.lastAudioSendTimeSync = new Object();
        this.packetNumber = new AtomicInteger();
		this.packetNumber.set(0);
    	
        this.coord = new Coord(-1,-1,-1);
        this.preciseCoord = new CoordFloat(-1.0F,-1.0F,-1.0F);
        this.map.set(-1);
        this.id.set(-1);
        this.publicId = "";
        this.connectionState = PlayerState.EMPTY;
        this.groupId.set(-1);
        this.cryptoIsActive.set(false);
        this.caddress = new CAddress();
        
        this.needSendCoordPos.set(false);
        
        this.setMapSync = new Object();
        this.setCoord = new Object();
        
        this.sender = new SendDatagram();
        
        this.localTalk = new AtomicBoolean();
        this.localTalk.set(true);
        this.groupTalk = new AtomicBoolean();
        this.groupTalk.set(false);
        this.localListen = new AtomicBoolean();
        this.localListen.set(true);
        this.groupListen = new AtomicBoolean();
        this.groupListen.set(false);
    }
    
    @Override
    public Coord getCoord() {
    	synchronized(setCoord) {
    		return new Coord(coord);
    	}
	}

	@Override
    public void initCrypto(byte[] key) {
    	crypto = new Crypto(key);
    	cryptoIsActive.set(true);;
    }
	
	@Override
	public byte[] decrypt(byte[] data) {
		if(cryptoIsActive.get()) {
			return crypto.decrypt(data);
		}
		return new byte[0];
	}
	@Override
	public byte[] encrypt(byte[] data) {
		if(cryptoIsActive.get()) {
			return crypto.encrypt(data);
		}
		return new byte[0];
	}

    @Override
    public int getMap() {
        return map.get();
    }

    @Override
    public synchronized String getPublicId() {
        return new String(publicId);
    }

    @Override
    public int getID() {
        return id.get();
    }

    @Override
    public void setMap(int map) {
    	synchronized(setMapSync) {
	        if (map != this.map.get()) {
	    		if(MapsManager.existMap(map)) {
	    			unsafeRemoveFromMap();
		            
		            this.map.set(map);
		            MapsManager.getMap(this.map.get()).addPlayer(this);
		            MapsManager.getMap(this.map.get()).getChunkByPlayerCoords(coord).addPlayer(this);
	    		}
	        }
    	}
    }

    @Override
    public void setCoord(float x, float y, float z) {
    	synchronized(setCoord) {
	    	Coord newCoord = new Coord(Math.round(x), Math.round(y), Math.round(z));
	    	
	    	boolean canSetCoord = true;
	    	if(MapsManager.existMap(this.map.get())) {
		        if (!MapsManager.getMap(this.map.get()).getChunkByPlayerCoords(newCoord).havePlayer(id.get())) {
		            MapsManager.getMap(this.map.get()).getChunkByPlayerCoords(this.coord).removePlayer(id.get());
		            this.coord.setCoord(newCoord);
		            MapsManager.getMap(this.map.get()).getChunkByPlayerCoords(newCoord).addPlayer(this);
		        } else {
		        	canSetCoord = true;
		        }
	        } else {
	        	canSetCoord = true;
	        }
	    	
	    	if(canSetCoord) {
	    		this.coord.setCoord(newCoord);
	    		this.preciseCoord.setX(x);
		        this.preciseCoord.setY(y);
		        this.preciseCoord.setZ(z);
	    	}
    	}
    }
    
    @Override
    public void setPosition(int map, float x, float y, float z){
    	this.setMap(map);
    	this.setCoord(x,y,z);
    }
    
    private void unsafeRemoveFromMap() {
    	if (MapsManager.existMap(this.map.get())) {
            MapsManager.getMap(this.map.get()).removePlayer(this.id.get());
            MapsManager.getMap(this.map.get()).getChunkByPlayerCoords(this.coord).removePlayer(this.id.get());
        }
    }
    
    @Override
	public void removeFromMap() {
    	synchronized(setMapSync) {
    		unsafeRemoveFromMap();
		}
    }

    @Override
    public synchronized void setPublicId(String publicId) {
        this.publicId = publicId;
    }

    @Override
    public void setID(int id) {
        this.id.set(id);
    }
    
    @Override
	public void setTalkInLocal(boolean talk) {
    	localTalk.set(talk);
    }
    
    @Override
	public void setTalkInGroup(boolean talk) {
    	groupTalk.set(talk);
    }
    
    @Override
	public boolean canTalkInLocal() {
    	return localTalk.get();
    }
    
    @Override
	public boolean canTalkInGroup() {
    	return groupTalk.get();
    }
    
	
    @Override
	public void setListenLocal(boolean talk) {
    	localListen.set(talk);
    }
    
    @Override
	public void setListenGroup(boolean talk) {
    	groupListen.set(talk);
    }
    
    @Override
	public boolean canListenLocal() {
    	return localListen.get();
    }
    
    @Override
	public boolean canListenGroup() {
    	return groupListen.get();
    }


    @Override
    public synchronized void setGroupId(int roomId) {
    	int myRoomId = groupId.get();
    	if(myRoomId >= 0) {
    		if(RoomsManager.haveRoom(myRoomId)) {
    			Room myRoom = RoomsManager.getRoom(myRoomId);
    			myRoom.removePlayer(this.id.get());
    		}
    	}
    	if(roomId >= 0) {
    		if(RoomsManager.haveRoom(roomId)) {
    			Room myRoom = RoomsManager.getRoom(roomId);
    			myRoom.addPlayer(this.id.get(), this);
    		}
    	}
        this.groupId.set(roomId);
    }
    
    @Override
    public int getGroupId() {
        return groupId.get();
    }

    @Override
    public synchronized PlayerState getState() {
        return PlayerState.valueOf(connectionState.name());
    }

    @Override
    public synchronized void setConnectionState(PlayerState state) {
        this.connectionState = state;
    }
    
    @Override
    public synchronized void setAddress(InetAddress address) {
    	caddress.setAddress(address);
    }
	
    @Override
	public synchronized void setPort(int port) {
		caddress.setPort(port);
	}
    
    @Override
    public synchronized InetAddress getAddress() {
    	return caddress.getAddress();
    }
	
    @Override
	public synchronized int getPort() {
		return caddress.getPort();
	}
    
    @Override
    public void setEnqueueAudioTimeCount(int count) {
    	audioPacketNeededTime.set(count);
    }
    
    @Override
    public int getEnqueueAudioTimeCount() {
    	return audioPacketNeededTime.get();
    }
    
    private boolean buildBufferAudioPacketFromQueue(Server.Builder fromServerBuilderOut, ConcurrentLinkedQueue<PlayerPacketAudio> audioQueue) {
		if(audioQueue.isEmpty()) {
			return false;
		}
		
		PlayerPacketAudio packet = audioQueue.poll();
		if(packet == null) {
			return false;
		}
    	
    	
    	Server.Builder fromServerBuilder = Server.newBuilder();
    	
		if(packet.haveCoord()) {
			fromServerBuilder.setCoordX(packet.getCoord().getX());
			fromServerBuilder.setCoordY(packet.getCoord().getY());
			fromServerBuilder.setCoordZ(packet.getCoord().getZ());
		}
		
		int audioNumber = packet.getAudioNumber();
		
		if(!isValidNumberPacketAudio(audioNumber)) {
			return false;
		}
		
		Server fromServer = fromServerBuilder.setId(packet.getPlayer().getID())
				.setAudio(ByteString.copyFrom(packet.getAudio()))
				.setSampleTime(packet.getSampleTime())
				.setAudioNum(audioNumber)
				.setIsGroup(!packet.isLocalAudio())
				.build();
		byte[] sendBuffer= fromServer.toByteArray();
		
		//System.out.println("Audio " + audioNumber + " from tail");
		
		fromServerBuilderOut.addExtraClientMSG(ByteString.copyFrom(sendBuffer));
		
		buildBufferAudioPacketFromQueue(fromServerBuilderOut, audioQueue);
		return true;
    	
    }
    
    ConcurrentLinkedQueue<PlayerPacketAudio> checkAudioPacketQueuePlayer(int id) {
    	if(!audioPacketQueue.containsKey(id)) {
    		audioPacketQueue.put(id, new ConcurrentLinkedQueue<>());
    	}
    	return audioPacketQueue.get(id);
    }
    
    void removePlayerFromAudioQueue(int id) {
    	if(audioPacketQueue.containsKey(id)) {
    		audioPacketQueue.remove(id);
    	}
    }

    @Override
    public boolean sendAudioPacketToMe(PlayerPacketAudio packet){
    	//System.out.println("AddAud " + packet.getAudioNumber() + " from tail");
    	int playerId = packet.getPlayer().getID();
    	ConcurrentLinkedQueue<PlayerPacketAudio> audioPlayerQueue = checkAudioPacketQueuePlayer(playerId);
    	
    	audioPlayerQueue.add(packet);
    	
    	boolean canSkipTime = false;
    	Instant actualTime = Instant.now();
    	synchronized(lastAudioSendTimeSync) {
    		int comparisonResult = (int) actualTime.minusMillis(lastAudioSendTime.toEpochMilli()).toEpochMilli();
    		if(comparisonResult >= audioPacketNeededTime.get()) {
    			canSkipTime = true;
    		}
		}
    	
    	//if(apeTime >= audioPacketNeededTime.get() || canSkipTime) {
    	if(canSkipTime) {
    		Server.Builder fromServerBuilder = Server.newBuilder();
    		boolean packPackets = buildBufferAudioPacketFromQueue(fromServerBuilder, audioPlayerQueue);
    		removePlayerFromAudioQueue(playerId);
    		if(!packPackets) {
    			return false;
    		}
    		
    		Instant currentTime = Instant.now();
    		com.google.protobuf.Timestamp.Builder timebuilder = com.google.protobuf.Timestamp.newBuilder();
    		timebuilder.setSeconds(currentTime.getEpochSecond());
    		timebuilder.setNanos(currentTime.getNano());
    		fromServerBuilder.setPacketTime(timebuilder.build());
    		int packetNumb =  packetNumber.updateAndGet(n -> (n >= 256) ? 0 : n + 1);
    		fromServerBuilder.setAudioNum(packetNumb);
    		Server fromServer = fromServerBuilder.build();
    		
    		byte[] sendBuffer = fromServer.toByteArray();
    		if(UDPServer.isNeedCryptograph()) {
    			sendBuffer = encrypt(fromServer.toByteArray());
    		}
    		sender.send(getAddress(), getPort(), sendBuffer);
    		//System.out.println("Sended " + fromServer.getExtraClientMSGCount() + " audio's");
    		//System.out.println("NeedTime " + audioPacketNeededTime.get());
    		synchronized(lastAudioSendTimeSync) {
    			lastAudioSendTime = Instant.now();
    		}
    		return true;
    	}
    	
    	return true;
    	/*
    	if(groupParticipate.get() && packet.getAudioType() == PlayerAudioType.TOGROUP) {
    		
    		return true;
    	} else if(groupParticipate.get() == false && packet.getAudioType() == PlayerAudioType.TOGERAL) {
    		Server.Builder fromServerBuilder = Server.newBuilder();
    		
			if(packet.haveCoord()) {
    			fromServerBuilder.setCoordX(packet.getCoord().getX());
    			fromServerBuilder.setCoordX(packet.getCoord().getX());
    			fromServerBuilder.setCoordX(packet.getCoord().getX());
			}
    		
    		int audioNumber = packet.getAudioNumber();
    		
    		if(!isValidNumberPacketAudio(audioNumber)) {
    			return false;
    		}
    		
    		Server fromServer = fromServerBuilder.setId(packet.getPlayer().getID())
    				.setAudio(ByteString.copyFrom(packet.getAudio()))
    				.setSampleTime(packet.getSampleTime())
    				.setAudioNum(audioNumber)
    				.build();
    		byte[] sendBuffer = fromServer.toByteArray();
    		
    		if(UDPServer.isNeedCryptograph()) {
    			sendBuffer = encrypt(fromServer.toByteArray());
    		}
    		
    		sender.send(getAddress(), getPort(), sendBuffer);
    		return true;
    	}
        return false;*/
    }
    
    @Override
    public boolean isBlockedUser(int id) {
    	if(blockedUsers.containsKey(id)) {
    		return true;
    	}
    	return false;
    }
	
    @Override
	public void blockUser(Player player) {
    	if(!blockedUsers.containsKey(player.getID())) {
    		blockedUsers.put(player.getID(), player);
    	}
    }
    
    @Override
	public void unBlockUser(Player player) {
    	if(blockedUsers.containsKey(player.getID())) {
    		blockedUsers.remove(player.getID());
    	}
    }
    
    @Override
    public boolean isValidNumberPacketAudio(int nb) {
    	if(nb > 255 || nb < 0) {
    		return false;
    	}
    	return true;
    }

	@Override
	public int getScretID() {
		return secret_id.get();
	}

	@Override
	public void setScretID(int Secret_id) {
		this.secret_id.set(Secret_id);
		
	}

	@Override
	public CoordFloat getPreciseCoord() {
		synchronized(setCoord) {
			return new CoordFloat(preciseCoord);
		}
	}

}
