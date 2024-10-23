package pasaud.voip;

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.time.Instant;
import java.util.concurrent.TimeUnit;

import com.google.protobuf.InvalidProtocolBufferException;

import pasaud.voip.player.Player;
import pasaud.voip.player.PlayerAntiSpam;
import pasaud.voip.player.PlayersManager;
import pasaud.voip.player.audio.PlayerPacketAudio;
import pasaud.voip.protocol.InvalidSession;
import pasaud.voip.protocol.SessionInfo;
import pasaud.voip.protocol.ValidSession;
import pasaud.voip.protocol.Protocol.Server;
import pasaud.voip.protocol.Protocol;
import pasaud.voip.types.CAddress;
import pasaud.voip.types.CoordFloat;

public class ProtoParser {

	private byte[] buffer;
    private final InetAddress address;
    private final int port;
    
    private final CAddress caddress;
    
    private final boolean acceptReceivedCoords;
    
    private boolean needEncrypt;

    public ProtoParser(DatagramPacket packet, boolean needDecrypt, boolean acceptReceivedCoords) {
    	this.needEncrypt = needDecrypt;
    	this.buffer = new byte[packet.getLength()];
    	System.arraycopy(packet.getData(), 0, this.buffer, 0, this.buffer.length);
        this.address = packet.getAddress();
        this.port = packet.getPort();
        caddress = new CAddress(address, port);
        this.acceptReceivedCoords = acceptReceivedCoords;
    }
    
    private boolean checkConnect(Protocol.Client pbuffer) {
    	if(pbuffer.hasSecretId()) {
    		boolean canSendValidSession = false;
    		int playerId = -1;
    		
			if(PlayersManager.containsPreConnectedPlayerBySecretId(pbuffer.getSecretId())) {
				Player player = PlayersManager.getPreConnectedPlayerByScretId(pbuffer.getSecretId());
				player.setAddress(address);
				player.setPort(port);
				PlayersManager.addConnect(PlayersManager.getPreConnectedPlayerByScretId(pbuffer.getSecretId()));
				
				playerId = player.getID();
				canSendValidSession = true;
			} else if (PlayersManager.containsPlayerByAddress(caddress)) {
				Player player = PlayersManager.getPlayerByAddress(caddress);
				playerId = player.getID();
				canSendValidSession = true;
			}
			
			if(canSendValidSession) {
				SessionInfo validate = new ValidSession(address, port);
				validate.setId(playerId);
				validate.send();
				return true;
			}
			return false;
		}
    	return false;
    }
    
    public Protocol.Client parseFromClientAudio(Protocol.ClientBase base) throws InvalidProtocolBufferException{
    	if(base.hasClientExtAudio()) {
	    	Protocol.ClientAudio audPbuffer = base.getClientExtAudio();
			Protocol.Client.Builder fromClientBuilder = Protocol.Client.newBuilder();
			fromClientBuilder.setId(audPbuffer.getId());
			fromClientBuilder.setAudio(audPbuffer.getAudio());
			fromClientBuilder.setAudioNum(audPbuffer.getAudioNum());
			fromClientBuilder.setSampleTime(audPbuffer.getSampleTime());
			return fromClientBuilder.build();
    	} else {
    		return base.getClientExt();
    	}
    }
    
    public void run() {
    	try {
    		
    		PlayerAntiSpam.get().attPacketTime(caddress);
    		
    		if(PlayerAntiSpam.get().isBlocked(caddress)) {
    			return;
    		}

    		Protocol.ClientBase pbuffer = Protocol.ClientBase.newBuilder().build();
    		
    		if(needEncrypt) {
    			boolean checkConnectSesssion = false;
    			try {
    				if(PlayersManager.containsPlayerByAddress(caddress)) {
    					Player player = PlayersManager.getPlayerByAddress(caddress);
    					byte[] bufferDrcpt = player.decrypt(buffer);
    					if(bufferDrcpt.length > 0) {
    						this.buffer = bufferDrcpt;
    					} else {
    						//try First Connection handshake
            				checkConnectSesssion = true;
    					}
    					pbuffer = Protocol.ClientBase.parseFrom(buffer);
    				} else {
    					//try First Connection handshake
        				checkConnectSesssion = true;
        				pbuffer = Protocol.ClientBase.parseFrom(buffer);
    				}
    			} catch (InvalidProtocolBufferException e) {
					PlayerAntiSpam.get().addFailedConnect(caddress);
    				if(PlayerAntiSpam.get().isBlocked(caddress)) {
    					SessionInfo invalidate = new InvalidSession(address, port);
    					invalidate.send();
    				}
    				System.out.println("Ilegible Packet from: " + caddress.toString());
    				return;
    			}
    			
    			if(checkConnectSesssion) {
    				if(pbuffer.hasClientExt()) {
	    				if(checkConnect(pbuffer.getClientExt())) {
	    					System.out.println("Valid packet not encrypted from: " + caddress.toString());
	    				} else {
	    					System.out.println("Invalid packet from: " + caddress.toString());
	    				}
	    				return;
    				}
    			}
    			
    		} else {
				pbuffer = Protocol.ClientBase.parseFrom(buffer);
				if(pbuffer.hasClientExt()) {
					checkConnect(pbuffer.getClientExt());
				}
    		}
    		
    		Protocol.Client pbufferExt = Protocol.Client.newBuilder().build();
    		
    		pbufferExt = parseFromClientAudio(pbuffer);
    		
    		PlayerAntiSpam.get().sucessLegiblePacket(caddress);
    		
    		if(PlayersManager.containsPlayerByAddress(caddress)) {
    			Player player = PlayersManager.getPlayerByAddress(caddress);
    			
    			if(pbufferExt.hasPacketTime()) {
    				 long packtTimeCreated = TimeUnit.SECONDS.toMillis(pbufferExt.getPacketTime().getSeconds()) + 
    						 TimeUnit.NANOSECONDS.toMillis(pbufferExt.getPacketTime().getNanos());
    				 Instant currentTime = Instant.now();
					 long lPing = (currentTime.toEpochMilli() - packtTimeCreated + player.getEnqueueAudioTimeCount()) / 2;
					 int ping = 0;
					 if (lPing >= Integer.MIN_VALUE && lPing <= Integer.MAX_VALUE) {
					 	ping = (int) lPing;
					 	if(ping < 0) {
					 		ping = 0;
					 	}
					 } else {
						ping = 0;
					 }

					 System.out.println("Player " + player.getID() + " ping: " + ping);
					 player.setEnqueueAudioTimeCount(ping);
    			}
    					
    			boolean changeCoord = false;
    			CoordFloat coord = player.getPreciseCoord();
    			if(acceptReceivedCoords) {
	    			if(pbufferExt.hasCoordX()) {
	    				coord.setX(pbufferExt.getCoordX());
	    				changeCoord = true;
	    			}
	    			if(pbufferExt.hasCoordY()) {
	    				coord.setY(pbufferExt.getCoordY());
	    				changeCoord = true;
	    			}
	    			if(pbufferExt.hasCoordZ()) {
	    				coord.setZ(pbufferExt.getCoordZ());
	    				changeCoord = true;
	    			}
	    			if(pbufferExt.hasMapNum()) {
	    				player.setMap(pbufferExt.getMapNum());
	    			}
	    			if(changeCoord) {
	    				player.setCoord(coord.getX(), coord.getY(), coord.getZ());
	    			}
    			}
    			
    			if(pbufferExt.hasAudio()) {
    				byte[] audioByte = pbufferExt.getAudio().toByteArray();
    				
    				int audioNumber = 0;
    				if(pbufferExt.hasAudioNum()) {
    					audioNumber = pbufferExt.getAudioNum();
    				}
    				
    				PlayerPacketAudio audioPacket = 
    						new PlayerPacketAudio(player, audioNumber, audioByte, pbufferExt.getSampleTime(), player.getGroupId());
    				audioPacket.setCoord(coord);
    				AudioPropage reverb = new AudioPropage(audioPacket);
    				reverb.run();
    			}
    		} else {
    			
    		}
		} catch (InvalidProtocolBufferException e) {
			PlayerAntiSpam.get().addFailedConnect(caddress);
			if(PlayerAntiSpam.get().isBlocked(caddress)) {
				SessionInfo invalidate = new InvalidSession(address, port);
				invalidate.send();
			}
			return;
		}
    }
	
}
