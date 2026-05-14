/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package thread;

import model.Server;
import view.ServerDetailView;

import javax.swing.*;

/**
 *
 * @author mahar
 */
public class MonitorThread extends Thread {
    private Server server;
    private ServerDetailView view;
    private volatile boolean isRunning;

    public MonitorThread(Server server, ServerDetailView view) {
        this.server = server;
        this.view = view;
        this.isRunning = true;
    }

    @Override
    public void run() {
        while (isRunning) {
            try {
                if (server.getStatus().equalsIgnoreCase("Online")) {
                    double fluctuateCpu = server.getCpuUtilization() + (Math.random() * 4 - 2);
                    double fluctuateRam = server.getRamUtilization() + (Math.random() * 4 - 2);

                    fluctuateCpu = Math.max(0, Math.min(100, fluctuateCpu));
                    fluctuateRam = Math.max(0, Math.min(100, fluctuateRam));

                    server.setCpuUtilization(fluctuateCpu);
                    server.setRamUtilization(fluctuateRam);
                }

                SwingUtilities.invokeLater(() -> {
                    view.updateCpuBar(server.getCpuUtilization());
                    view.updateRamBar(server.getRamUtilization());
                });
                Thread.sleep(1500);
            } catch (InterruptedException e) {
                System.out.println("Monitoring Thread untuk aset " + server.getIdAsset() + " dihentikan.");
                isRunning = false;
            }
        }
    }

    public void stopMonitoring() {
        this.isRunning = false;
        this.interrupt();
    }
}
