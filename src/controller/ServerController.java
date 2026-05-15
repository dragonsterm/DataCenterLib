/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import dao.ServerDAO;
import model.Server;
import thread.MonitorThread;
import view.ServerDetailView;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 *
 * @author mahar
 */
public class ServerController {
    private ServerDetailView serverView;
    private ServerDAO serverDAO;
    private MonitorThread statThread;

    private Server currentServer;

    public ServerController(ServerDAO serverDAO) {
        this.serverDAO = serverDAO;
        this.serverView = new ServerDetailView();

        initViewListeners();
    }

    private void initViewListeners() {
        serverView.getBtnSetUtil().addActionListener(e -> {
            try {
                double cpuInput = Double.parseDouble(serverView.getInputCpu());
                double ramInput = Double.parseDouble(serverView.getInputRam());
                int storageInput = Integer.parseInt(serverView.getInputStorage());
                updateManualUtilization(cpuInput, ramInput, storageInput);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(serverView, "Input Must Be Numbers");
            }
        });
    }

    public void loadServerInfo(String serverId) {
        this.currentServer = serverDAO.read(serverId);

        if (currentServer != null) {
            serverView.displayServerStats(currentServer);
            startMonitoring();

            JDialog dialog = new JDialog();
            dialog.setTitle("Server Control Panel");
            dialog.setSize(450, 400);
            dialog.setLocationRelativeTo(null);
            dialog.add(serverView);

            dialog.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent e) {
                    if (statThread != null && statThread.isAlive()) {
                        statThread.stopMonitoring();
                    }
                }
            });
            dialog.setVisible(true);
        }
    }

    public void updateManualUtilization(double cpu, double ram, int storage) {
        if (currentServer != null) {
            String selectedStatus = serverView.getSelectedStatus();

            currentServer.setStatus(selectedStatus);
            currentServer.setCpuUtilization(cpu);
            currentServer.setRamUtilization(ram);
            currentServer.setUsedStorageGB(storage);

            boolean isSuccess = serverDAO.update(currentServer);

            if (isSuccess) {
                JOptionPane.showMessageDialog(serverView, "Server Data" + currentServer.getIdAsset() + " Successfully Updated");
                serverView.displayServerStats(currentServer);
            } else {
                JOptionPane.showMessageDialog(serverView, "Failed to Update");
            }
        }
    }

    public void startMonitoring() {
        if (statThread != null && statThread.isAlive()) {
            statThread.stopMonitoring();
        }

        statThread = new MonitorThread(currentServer, serverView);
        statThread.start();
    }
}
