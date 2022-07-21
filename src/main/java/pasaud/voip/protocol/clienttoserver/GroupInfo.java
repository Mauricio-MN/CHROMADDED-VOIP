/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Enum.java to edit this template
 */
package pasaud.voip.protocol.clienttoserver;

/**
 *
 * @author wghat
 */
public enum GroupInfo {
    CLIENTTOKEN(8), PARTICIPATE(1), GROUPID(4);

    private final int size;
    GroupInfo(int i) {
        size = i;
    }

    public int getSize() {
        return size;
    }

}
