/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author mahar
 */
public class Server extends ComputingDevice {
    private String osType;
    private double cpuUtilization;
    private double ramUtilization;
    private int totalStorageGB;
    private int usedStorageGB;

    public Server(String idAsset, String modelName, int sizeInU, String status, int cpuCores, int totalRamGB,
                  String osType, int totalStorageGB, int usedStorageGB) {
        super(idAsset, modelName, sizeInU, status, cpuCores, totalRamGB);
        this.osType = osType;
        this.totalStorageGB = totalStorageGB;
        this.usedStorageGB = usedStorageGB;
        this.cpuUtilization = 0.0;
        this.ramUtilization = 0.0;

        // Memicu validasi status walau saat pertama kali dibentuk
        this.setStatus(status);
    }

    @Override
    public String getDetails() {
        return "Server [" + getIdAsset() + "] - Model: " + getModelName() +
                " | OS: " + osType + " | Disk: " + usedStorageGB + "GB/" + totalStorageGB + "GB";
    }

    @Override
    public double calculateLoad() {
        // Contoh kalkulasi beban sederhana (rata-rata dari persentase CPU dan RAM)
        return (cpuUtilization + ramUtilization) / 2.0;
    }

    @Override
    public void setStatus(String status) {
        super.setStatus(status); // Set status default di parent class

        // Memaksa utilitas 0 jika dialihkan kesalah satu dari status ini
        if ("Offline".equalsIgnoreCase(status) || "Maintenance".equalsIgnoreCase(status)) {
            this.cpuUtilization = 0.0;
            this.ramUtilization = 0.0;
        }
    }

    public double getCpuUtilization() {
        return cpuUtilization;
    }

    public void setCpuUtilization(double cpuUtilization) {
        this.cpuUtilization = cpuUtilization;
    }

    public double getRamUtilization() {
        return ramUtilization;
    }

    public void setRamUtilization(double ramUtilization) {
        this.ramUtilization = ramUtilization;
    }

    public String getOsType() {
        return osType;
    }

    public int getTotalStorageGB() {
        return totalStorageGB;
    }

    public int getUsedStorageGB() {
        return usedStorageGB;
    }


    @Override
    public double getTemperature() {
        return 35.0 + (cpuUtilization * 0.5);
    }

    @Override
    public double getPowerDraw() {
        return 150.0 + (calculateLoad() * 2.0);
    }

    @Override
    public void turnOn() {
        setStatus("Online");
    }

    @Override
    public void turnOff() {
        setStatus("Offline");
    }

    @Override
    public void restart() {
        setStatus("Offline");
        setStatus("Online");
    }
}
