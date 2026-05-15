/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package thread;

import dao.ServerDAO;
import model.Server;
import model.ServerRack;
import view.BatchProgressDialog;

import java.util.List;
import javax.swing.*;

/**
 *
 * @author mahar
 */
public class BatchOperationThread extends Thread {
    private String operationType;
    private List<Server> serversList;
    private String targetRackId;
    private ServerDAO serverDAO;
    private ServerRack sourceRack;
    private ServerRack destinationRack;
    private BatchProgressDialog progressView;

    public BatchOperationThread(String operationType, List<Server> serversList, String targetRackId, ServerRack sourceRack, ServerDAO serverDAO, BatchProgressDialog progressView) {
        this.operationType = operationType;
        this.serversList = serversList;
        this.targetRackId = targetRackId;
        this.sourceRack = sourceRack;
        this.serverDAO = serverDAO;
        this.progressView = progressView;
    }

    private Runnable onSuccessCallback;

    public void setDestinationRack(ServerRack destinationRack) {
        this.destinationRack = destinationRack;
    }

    public void setOnSuccessCallback(Runnable callback) {
        this.onSuccessCallback = callback;
    }

    @Override
    public void run() {
        SwingUtilities.invokeLater(() -> progressView.setVisible(true));
        switch (operationType.toUpperCase()) {
            case "ADD":
                processAdd();
                break;
            case "UPDATE":
                processUpdate();
                break;
            case "DELETE":
                processDelete();
                break;
            case "MOVE":
                processMove();
                break;
        }
        SwingUtilities.invokeLater(() -> {
            progressView.dispose();
            if (onSuccessCallback != null) {
                onSuccessCallback.run();
            }
        });
    }
    private void processAdd() {
        int total = serversList.size();
        for (int i = 0; i < total; i++) {
            Server s = serversList.get(i);
            boolean dbSuccess = serverDAO.create(s);
            if (dbSuccess && sourceRack != null) {
                sourceRack.addEquipment(s, s.getStartSlot());
            }
            updateProgress(i + 1, total, "Menambahkan server " + s.getIdAsset());
            simulateDelay(500);
        }
    }

    private void processUpdate() {
        int total = serversList.size();
        for (int i = 0; i < total; i++) {
            Server s = serversList.get(i);
            serverDAO.update(s);

            if (sourceRack != null) {
                sourceRack.removeEquipment(s.getIdAsset());
                sourceRack.addEquipment(s, s.getStartSlot());
            }
            updateProgress(i + 1, total, "Memperbarui data server " + s.getIdAsset());
            simulateDelay(300);
        }
    }

    private void processDelete() {
        int total = serversList.size();
        for (int i = 0; i < total; i++) {
            Server s = serversList.get(i);
            serverDAO.delete(s.getIdAsset());
            if (sourceRack != null) {
                sourceRack.removeEquipment(s.getIdAsset());
            }
            updateProgress(i + 1, total, "Menghapus server " + s.getIdAsset());
            simulateDelay(300);
        }
    }

    private void processMove() {
        int total = serversList.size();
        for (int i = 0; i < total; i++) {
            Server s = serversList.get(i);
            boolean dbSuccess = serverDAO.moveServerRack(s.getIdAsset(), targetRackId);
            if (dbSuccess) {
                if (sourceRack != null) {
                    sourceRack.removeEquipment(s.getIdAsset());
                }
                if (destinationRack != null) {
                    s.setRackId(targetRackId);
                    destinationRack.addEquipment(s, s.getStartSlot());
                }
            }
            updateProgress(i + 1, total, "Memindahkan server " + s.getIdAsset() + " ke rak " + targetRackId);
            simulateDelay(400);
        }
    }

    private void updateProgress(int current, int total, String msg) {
        SwingUtilities.invokeLater(() -> progressView.updateProgress(current, total, msg));
    }

    private void simulateDelay(int ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
