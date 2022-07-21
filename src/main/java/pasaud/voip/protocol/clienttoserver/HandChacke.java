/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Enum.java to edit this template
 */
package pasaud.voip.protocol.clienttoserver;

/**
 *
 * @author wghat
 */
public enum HandChacke{
    CLIENTTOKEN(8), CLIENTNAME(20), PERSONALNUMBER(8);

    private final int size;
    HandChacke(int i) {
        size = i;
    }

    public int getSize() {
        return size;
    }

}
