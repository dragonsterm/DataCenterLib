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
    private List<ServerRack> racks;

    public DataCenterRoom(String roomName) {
        this.roomName = roomName;
        this.racks = new ArrayList<>();
    }

    public void addRack(ServerRack rack) {
        if (rack != null && findRackById(rack.getRackId()) == null) {
            this.racks.add(rack);
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

    public List<ServerRack> getAllRacks() {
        return racks;
    }

    public String getRoomName() {
        return roomName;
    }
}
