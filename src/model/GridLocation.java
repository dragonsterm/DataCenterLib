/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author mahar
 */
public class GridLocation {
    private int xCoordinate;
    private int yCoordinate;
    private String zoneName;

    public GridLocation(int xCoordinate, int yCoordinate, String zoneName) {
        this.xCoordinate = xCoordinate;
        this.yCoordinate = yCoordinate;
        this.zoneName = zoneName;
    }

    public String getLocationString() {
        return "Zone " + zoneName + " [" + xCoordinate + "," + yCoordinate + "]";
    }

    public int getxCoordinate() {
        return xCoordinate;
    }
    public int getyCoordinate() {
        return yCoordinate;
    }
    public String getZoneName() {
        return zoneName;
    }
}
