
package pasaud.voip.player.audio;

public class PlayerPacketAudio {

    private String playerHash;
    private byte[] audio;
    PlayerAudioType audioType;

    public PlayerPacketAudio(String player, byte[] audio, PlayerAudioType audioType) {
        this.playerHash = player;
        this.audio = audio;
        this.audioType = audioType;
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

}
