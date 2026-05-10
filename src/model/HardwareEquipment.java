/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author mahar
 */
public abstract class HardwareEquipment implements IManageable, IMonitorable{
    private String idAsset;
    private String modelName;
    private int sizeInU;
    private String status;

    public HardwareEquipment(String idAsset, String modelName, int sizeInU, String status) {
        this.idAsset = idAsset;
        this.modelName = modelName;
        this.sizeInU = sizeInU;
        this.status = status;
    }

    public abstract String getDetails();

    public String getIdAsset() {
        return idAsset;
    }

    public String getModelName() {
        return modelName;
    }

    public int getSizeInU() {
        return sizeInU;
    }

    @Override
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
