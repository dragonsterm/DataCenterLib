/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author mahar
 */
public abstract class ComputingDevice extends HardwareEquipment {
    private int cpuCores;
    private int totalRamGB;

    public ComputingDevice(String idAsset, String modelName, int sizeInU, String status, int cpuCores, int totalRamGB) {
        super(idAsset, modelName, sizeInU, status);
        this.cpuCores = cpuCores;
        this.totalRamGB = totalRamGB;
    }

    public abstract double calculateLoad();

    public int getCpuCores() {
        return cpuCores;
    }

    public int getTotalRamGB() {
        return totalRamGB;
    }
}
