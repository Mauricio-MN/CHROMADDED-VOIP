/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Enum.java to edit this template
 */
package pasaud.voip.protocol.clienttoserver;

/**
 *
 * @author wghat
 */
public enum Disconnect {
    CLIENTTOKEN(8);

    private final int size;
    Disconnect(int i) {
        size = i;
    }

    public int getSize() {
        return size;
    }
}
