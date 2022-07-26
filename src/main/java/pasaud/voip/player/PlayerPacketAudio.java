/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pasaud.voip.player;

/**
 *
 * @author wghat
 */
public class PlayerPacketAudio {

    private Player player;
    private byte[] audio;

    PlayerPacketAudio(Player player, byte[] audio) {
        this.player = player;
        this.audio = audio;
    }

    public Player getPlayer() {
        return player;
    }

    public byte[] getAudio() {
        return audio;
    }

}
