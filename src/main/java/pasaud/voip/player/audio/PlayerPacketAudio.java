
package pasaud.voip.player.audio;

import pasaud.voip.player.Player;
import pasaud.voip.types.CoordFloat;

public class PlayerPacketAudio {

    private final byte[] audio;
    private Player player;
    private final int audioNumber;
    private final int audioGroup;
    private final int sampleTime;
    
    private boolean _haveCoord;
    private CoordFloat coord;
    
    private boolean localAudio;

    public PlayerPacketAudio(Player player, Integer audioNumber, byte[] audio, int sampleTime, int audioGroup) {
    	this.player = player;
    	this.audioGroup = audioGroup;
        this.audio = audio;
        this.audioNumber = audioNumber;
        this.sampleTime = sampleTime;
        this._haveCoord = false;
        this.coord = new CoordFloat(0,0,0);
        this.localAudio = true;
    }
    
    public void setCoord(CoordFloat coord) {
    	this._haveCoord = true;
    	this.coord = coord;
    }
    
    public boolean haveCoord() {
    	return _haveCoord;
    }
    
    public CoordFloat getCoord() {
    	return new CoordFloat(coord);
    }
    
    public byte[] getAudio() {
        return audio;
    }

    public Player getPlayer() {
        return player;
    }
    
    public int getAudioNumber() {
    	return audioNumber;
    }
    
    public int getGroupId() {
    	return audioGroup;
    }
    
    public int getSampleTime() {
    	return sampleTime;
    }

	public boolean isLocalAudio() {
		return localAudio;
	}

	public void setLocalAudio(boolean localAudio) {
		this.localAudio = localAudio;
	}

}
