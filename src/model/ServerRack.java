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
public class ServerRack {
    private String rackId;
    private int maxCapacityU;
    private int currentUsedU;
    private GridLocation location;
    private HardwareEquipment[] slots;

    public ServerRack(String rackId, int maxCapacityU, GridLocation location) {
        this.rackId = rackId;
        this.maxCapacityU = maxCapacityU;
        this.location = location;
        this.slots = new HardwareEquipment[maxCapacityU];
        this.currentUsedU = 0;
    }

    public synchronized boolean addEquipment(HardwareEquipment hw, int startSlot) {
        if (hw == null || startSlot < 0)
            return false;

        int size = hw.getSizeInU();

        if (startSlot + size > maxCapacityU) {
            return false;
        }

        for (int i = startSlot; i < startSlot + size; i++) {
            if (slots[i] != null) {
                return false;
            }
        }

        for (int i = startSlot; i < startSlot + size; i++) {
            slots[i] = hw;
        }

        currentUsedU += size;
        return true;
    }

    public synchronized boolean removeEquipment(String idAsset) {
        boolean foundAndRemoved = false;
        for (int i = 0; i < maxCapacityU; i++) {
            if (slots[i] != null && slots[i].getIdAsset().equals(idAsset)) {
                currentUsedU--;
                slots[i] = null;
                foundAndRemoved = true;
            }
        }
        return foundAndRemoved;
    }

    public boolean canAccommodate(int requiredU) {
        return getAvailableU() >= requiredU;
    }

    public int getAvailableU() {
        return maxCapacityU - currentUsedU;
    }

    public String[] getVisualRepresentation() {
        String[] visual = new String[maxCapacityU];
        for (int i = 0; i < maxCapacityU; i++) {
            if (slots[i] == null) {
                visual[i] = "[Slot " + i + "] - EMPTY";
            } else {
                visual[i] = "[Slot " + i + "] - " + slots[i].getIdAsset() + " (" + slots[i].getModelName() + ")";
            }
        }
        return visual;
    }

    public String getRackId() {
        return rackId;
    }

    public List<HardwareEquipment> getHardwareList() {
        List<HardwareEquipment> uniqueEquipments = new ArrayList<>();
        for (HardwareEquipment hw : slots) {
            if (hw != null && !uniqueEquipments.contains(hw)) {
                uniqueEquipments.add(hw);
            }
        }
        return uniqueEquipments;
    }

    public GridLocation getLocation() {
        return location;
    }

    public int getMaxCapacityU() {
        return maxCapacityU;
    }
}
