/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Enum.java to edit this template
 */
package pasaud.voip.protocol.clienttoserver;

/**
 *
 * @author wghat
 */
public enum AudioInfo {
    CLIENTTOKEN(8), AUDIO(256);

    private final int size;
    AudioInfo(int i) {
        size = i;
    }

    public int getSize() {
        return size;
    }

}
