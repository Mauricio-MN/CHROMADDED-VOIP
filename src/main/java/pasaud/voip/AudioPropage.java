package pasaud.voip;

import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

import pasaud.voip.maps.Chunk;
import pasaud.voip.maps.ServerMap;
import pasaud.voip.maps.MapsManager;
import pasaud.voip.player.Player;
import pasaud.voip.player.audio.PlayerPacketAudio;
import pasaud.voip.types.Coord;
import pasaud.voip.rooms.*;

public class AudioPropage{

    private ServerMap map;
    private PlayerPacketAudio packet;

    private Coord chunkCoord;
    private Coord playerCoord;
    
    private boolean talkInRoom;
    private int talkRoomId;
    
    private boolean talkInLocal;

    private boolean canRun;
    
    
    public AudioPropage(PlayerPacketAudio packet) {
        this.packet = packet;
        Player player = packet.getPlayer();
        playerCoord = player.getCoord();
        talkInRoom = player.canTalkInGroup();
        talkRoomId = player.getGroupId();
        talkInLocal = player.canTalkInLocal();
        map = MapsManager.getMap(player.getMap());
        chunkCoord = map.getChunkCoordsByPlayerCoords(playerCoord);
        canRun = true;
    }

    public static int distanceTo(int x, int y, int z, int toX, int toY, int toZ) {
        return (int) Math.sqrt(Math.pow(x - toX, 2) + Math.pow(y - toY, 2) + Math.pow(z - toZ, 2));
    }
    
    public static int distanceTo(Coord a, Coord b) {
        return (int) Math.sqrt(Math.pow(a.getX() - b.getX(), 2) + Math.pow(a.getY() - b.getY(), 2) + Math.pow(a.getZ() - b.getZ(), 2));
    }

    public void run() {
    	if(canRun) {
    		
    		HashMap<Integer, Player> playersSendedAud = new HashMap<>();
    		
    		if(talkInRoom) {
    			if(RoomsManager.haveRoom(talkRoomId)) {
    				Room myRoom = RoomsManager.getRoom(talkRoomId);
    				Player players[] = myRoom.getPlayers();
    				for (Player playerInRoom : players) {
                    	if(packet.getPlayer().getID() != playerInRoom.getID()) {
                            if (playerInRoom.canListenGroup()) {
                            	packet.setLocalAudio(false);
                            	playerInRoom.sendAudioPacketToMe(packet);
                            	playersSendedAud.put(playerInRoom.getID(), playerInRoom);
                            }
                        }
                    }
    			}
    		}
    		
    		if(!talkInLocal) {
    			return;
    		}

	        for (int rx = -1; rx <= 1; rx++) {
	            for (int ry = -1; ry <= 1; ry++) {
	                for (int rz = -1; rz <= 1; rz++) {
	                	Coord actualChunkCoord = new Coord(chunkCoord.getX() + rx, chunkCoord.getY() + ry, chunkCoord.getZ() + rz);
	                    if (map.containsChunk(actualChunkCoord)) {
	                        Chunk actualChunk = map.getChunk(actualChunkCoord);
	                        for (Player playerinChunk : actualChunk.getPlayers()) {
	                        	if(packet.getPlayer().getID() != playerinChunk.getID() || Main.debug == true) {
		                            Coord actualPlayerCoord = playerinChunk.getCoord();
		                            int distance = distanceTo(playerCoord, actualPlayerCoord);
		                            if (distance < map.getMaxAudioDistance() && distance > - map.getMaxAudioDistance()) {
		                            	if(playerinChunk.canListenLocal()) {
		                            		packet.setLocalAudio(true);
		                            		if(!playersSendedAud.containsKey(playerinChunk.getID())) {
		                            			playerinChunk.sendAudioPacketToMe(packet);
		                            		}
		                            	}
		                            }
	                        	}
	                        }
	                    }
	                }
	            }
	        }
	        
    	}
    }

}
