
package pasaud.voip.player.audio;

public class PlayerPacketAudio {

    private String playerHash;
    private byte[] audio;
    private PlayerAudioType audioType;
    private int id;
    private int number;

    public PlayerPacketAudio(int geralId, String hash, Integer number, byte[] audio, PlayerAudioType audioType) {
        this.playerHash = hash;
        this.audio = audio;
        this.audioType = audioType;
        this.number = number;
    }

    public PlayerAudioType getAudioType() {
        return audioType;
    }

    public String getPlayerHash() {
        return playerHash;
    }

    public byte[] getAudio() {
        return audio;
    }

    public int getID() {
        return id;
    }
    
    public int getNumber() {
    	return number;
    }

}
