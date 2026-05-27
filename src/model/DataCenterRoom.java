/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author mahar
 */
public class DataCenterRoom {
    private String roomName;
    private int widthGrid = 64;
    private int heightGrid = 64;
    private List<ServerRack> racks;
    private List<RoomPath> paths;

    public DataCenterRoom(String roomName) {
        this.roomName = roomName;
        this.racks = new ArrayList<>();
        this.paths = new ArrayList<>();
    }

    public void addRack(ServerRack rack) {
        if (rack != null && findRackById(rack.getRackId()) == null) {
            this.racks.add(rack);
        }
    }

    public void addRack(List<ServerRack> newRacks) {
        for (ServerRack rack : newRacks) {
            this.addRack(rack);
        }
    }

    public ServerRack findRackById(String rackId) {
        for (ServerRack rack : racks) {
            if (rack.getRackId().equals(rackId)) {
                return rack;
            }
        }
        return null;
    }

    public void addPath(RoomPath path) {
        this.paths.add(path);
    }

    public void setDimensions(int w, int h) {
        this.widthGrid = w;
        this.heightGrid = h;
    }

    public List<RoomPath> getPaths() {
        return paths;
    }

    public List<ServerRack> getAllRacks() {
        return racks;
    }

    public String getRoomName() {
        return roomName;
    }

    public int getWidthGrid() {
        return widthGrid;
    }
    public int getHeightGrid() {
        return heightGrid;
    }
}
