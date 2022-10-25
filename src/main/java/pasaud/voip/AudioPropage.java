package pasaud.voip;

import pasaud.voip.maps.Chunk;
import pasaud.voip.maps.Map;
import pasaud.voip.maps.MapsManager;
import pasaud.voip.player.Player;
import pasaud.voip.player.PlayersManager;
import pasaud.voip.player.audio.PlayerAudioType;
import pasaud.voip.player.audio.PlayerPacketAudio;

public class AudioPropage implements Runnable{

    private Map map;
    private PlayerPacketAudio packet;

    private int x;
    private int y;
    private int z;


    public AudioPropage(PlayerPacketAudio packet) {
        this.packet = packet;
        if(PlayersManager.containsPlayerConnectedByHashString(packet.getPlayerHash())){
            Player player = PlayersManager.getPlayerByHashString(packet.getPlayerHash());
            x = player.getXcoord();
            y = player.getYcoord();
            z = player.getZcoord();
            map = MapsManager.getMap(player.getMap());
        }
    }

    public static int distanceTo(int x, int y, int z, int toX, int toY, int toZ) {
        return (int) Math.sqrt(Math.pow(x - toX, 2) + Math.pow(y - toY, 2) + Math.pow(z - toZ, 2));
    }

    @Override
    public void run() {
        int[] coordsChunk = map.getChunkCoordsByPlayerCoords(x, y, z);

        for (int rx = -1; rx <= 1; rx++) {
            for (int ry = -1; ry <= 1; ry++) {
                for (int rz = -1; rz <= 1; rz++) {
                    if (map.containsChunk(coordsChunk[0] + rx, coordsChunk[1] + ry, coordsChunk[2] + rz)) {
                        Chunk actualChunk = map.getChunk(coordsChunk[0] + rx, coordsChunk[1] + ry, coordsChunk[2] + rz);
                        for (Player playerinChunk : actualChunk.getPlayers()) {
                            int vX = playerinChunk.getXcoord();
                            int vY = playerinChunk.getYcoord();
                            int vZ = playerinChunk.getZcoord();
                            int distance = distanceTo(x, y, z, vX, vY, vZ);
                            if (distance < map.getMaxAudioDistance() && distance > - map.getMaxAudioDistance()) {
                                playerinChunk.sendPacketToMe(PlayerAudioType.TOGERAL, packet);
                            }
                        }
                    }
                }
            }
        }
    }

}
