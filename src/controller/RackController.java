/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package controller;

import dao.ServerDAO;
import model.DataCenterRoom;
import model.Server;
import model.ServerRack;
import thread.BatchOperationThread;
import view.BatchServerFormView;
import view.BatchProgressDialog;
import view.RackDetailView;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author mahar
 */
public class RackController {
    private RackDetailView rackView;
    private BatchServerFormView batchFormView;
    private ServerDAO serverDAO;
    private ServerRack currentRack;
    private DataCenterRoom roomModel;

    public RackController(DataCenterRoom roomModel, ServerRack currentRack, ServerDAO serverDAO) {
        this.roomModel = roomModel;
        this.currentRack = currentRack;
        this.serverDAO = serverDAO;
        this.rackView = new RackDetailView();

        initViewListeners();
    }

    private void initViewListeners() {
        rackView.getBtnBatchAdd().addActionListener(e -> {
            java.util.List<Integer> selectedSlots = rackView.getSelectedEmptySlots();

            if (selectedSlots.isEmpty()) {
                JOptionPane.showMessageDialog(rackView, "Silakan klik/pilih minimal 1 slot kosong terlebih dahulu!");
            } else {
                openBatchForm("ADD", selectedSlots);
            }
        });

        rackView.getBtnBatchUpdate().addActionListener(e -> openBatchForm("UPDATE", rackView.getSelectedOccupiedSlots()));
        rackView.getBtnBatchMove().addActionListener(e -> openBatchForm("MOVE", rackView.getSelectedOccupiedSlots()));

        rackView.getBtnBatchDelete().addActionListener(e -> {
            java.util.List<Integer> selectedSlots = rackView.getSelectedOccupiedSlots();
            if(!selectedSlots.isEmpty()) {
                int dialogResult = JOptionPane.showConfirmDialog(rackView, "Apakah Anda yakin ingin menghapus server yang dipilih?", "Konfirmasi Hapus", JOptionPane.YES_NO_OPTION);
                if(dialogResult == JOptionPane.YES_OPTION) {
                    List<Server> serversToDelete = new ArrayList<>();
                    for(int slotIndex : selectedSlots) {
                        String slotData = rackView.getSlotButtons()[slotIndex].getText();
                        String serverId = extractServerIdFromSlotData(slotData);
                        if(serverId != null) {
                            Server s = serverDAO.read(serverId);
                            if (s != null) {
                                s.setRackId(currentRack.getRackId());
                                s.setStartSlot(slotIndex);
                                serversToDelete.add(s);
                            }
                        }
                    }
                    executeBatchOperation("DELETE", serversToDelete, currentRack.getRackId());
                }
            }
        });

        rackView.getBtnShowDetail().addActionListener(e -> {
            java.util.List<Integer> selectedSlots = rackView.getSelectedOccupiedSlots();
            if (selectedSlots.size() == 1) {
                int slotIndex = selectedSlots.get(0);
                String slotData = rackView.getSlotButtons()[slotIndex].getText();
                String serverId = extractServerIdFromSlotData(slotData);
                if (serverId != null) {
                    ServerController sc = new ServerController(serverDAO);
                    sc.setOnUpdateSuccessCallback(() -> {
                        Server updatedServer = serverDAO.read(serverId);
                        if (updatedServer != null) {
                            currentRack.removeEquipment(serverId);
                            currentRack.addEquipment(updatedServer, updatedServer.getStartSlot());
                        }
                        rackView.renderRackSlots(currentRack);
                    });

                    sc.loadServerInfo(serverId);
                }
            }
        });
    }

    private String extractServerIdFromSlotData(String slotData) {
        try {
            String[] parts = slotData.split(" - ");
            if (parts.length > 1) {
                String rightSide = parts[1];
                return rightSide.split(" ")[0];
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public void loadVisualRack(String rackId) {
        rackView.renderRackSlots(currentRack);
        rackView.setVisible(true);
    }

    public void openBatchForm(String operationType, java.util.List<Integer> selectedSlots) {
        List<String> availableRackIds = new ArrayList<>();
        if (roomModel != null && roomModel.getAllRacks() != null) {
            for (ServerRack r : roomModel.getAllRacks()) {
                availableRackIds.add(r.getRackId());
            }
        }
        batchFormView = new BatchServerFormView(currentRack.getAvailableU(), operationType, availableRackIds);

        int qty = selectedSlots.size();
        for (int i = 0; i < qty; i++) {
            int slotIdx = selectedSlots.get(i);
            if (operationType.equalsIgnoreCase("ADD")) {
                batchFormView.addFormRow(null);
            } else {
                String slotData = rackView.getSlotButtons()[slotIdx].getText();
                String serverId = extractServerIdFromSlotData(slotData);
                Server existingServer = serverDAO.read(serverId);
                batchFormView.addFormRow(existingServer);
            }
        }

        batchFormView.getBtnExecute().addActionListener(e -> {
            List<model.Server> dataInput = batchFormView.getBatchData();
            String target = batchFormView.getSelectedTargetRack();

            if (dataInput != null && !dataInput.isEmpty()) {
                List<Integer> availableSlots = new ArrayList<>();
                String[] visual = currentRack.getVisualRepresentation();
                for (int j = 0; j < visual.length; j++) {
                    if (visual[j].endsWith("EMPTY")) {
                        availableSlots.add(j);
                    }
                }

                int selectedIndex = 0;

                for (int i = 0; i < dataInput.size(); i++) {
                    Server s = dataInput.get(i);
                    s.setRackId(currentRack.getRackId());

                    if (operationType.equalsIgnoreCase("ADD")) {
                        if (selectedIndex < selectedSlots.size()) {
                            int slot = selectedSlots.get(selectedIndex);
                            s.setStartSlot(slot);
                            availableSlots.remove(Integer.valueOf(slot));
                            selectedIndex++;
                        } else {
                            if (!availableSlots.isEmpty()) {
                                int nextSlot = availableSlots.remove(0);
                                s.setStartSlot(nextSlot);
                            } else {
                                JOptionPane.showMessageDialog(batchFormView, "Kapasitas rak tidak mencukupi untuk semua server!");
                                return;
                            }
                        }
                    }
                }

                batchFormView.dispose();
                executeBatchOperation(operationType, dataInput, target);
            }
        });

        batchFormView.setVisible(true);
    }


    public void executeBatchOperation(String type, List<Server> servers, String targetRackId) {
        BatchProgressDialog progressDialog = new BatchProgressDialog();
        BatchOperationThread task = new BatchOperationThread(
                type, servers, targetRackId, currentRack, serverDAO, progressDialog
        );
        if (targetRackId != null && !targetRackId.isEmpty() && roomModel != null) {
            ServerRack target = roomModel.findRackById(targetRackId);
            task.setDestinationRack(target);
        }
        task.setOnSuccessCallback(() -> loadVisualRack(currentRack.getRackId()));
        task.start();
    }
}