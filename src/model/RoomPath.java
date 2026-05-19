/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author mahar
 */
public class RoomPath {
    private int idPath;
    private int idRoom;
    private int xCoord;
    private int yCoord;

    public RoomPath(int idPath, int idRoom, int xCoord, int yCoord) {
        this.idPath = idPath;
        this.idRoom = idRoom;
        this.xCoord = xCoord;
        this.yCoord = yCoord;
    }

    public int getXCoord() {
        return xCoord;
    }
    public int getYCoord() {
        return yCoord;
    }
    public int getIdPath() {
        return idPath;
    }
}