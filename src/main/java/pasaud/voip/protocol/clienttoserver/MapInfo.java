/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Enum.java to edit this template
 */
package pasaud.voip.protocol.clienttoserver;

/**
 *
 * @author wghat
 */
public enum MapInfo {
    CLIENTTOKEN(8), MAPID(4), MAPX(4), MAPY(4), MAPZ(4);

    private final int size;
    MapInfo(int i) {
        size = i;
    }

    public int getSize() {
        return size;
    }
}
