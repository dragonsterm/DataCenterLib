/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
        rackView.getBtnBatchAdd().addActionListener(e -> openBatchForm("ADD"));
        rackView.getBtnBatchUpdate().addActionListener(e -> openBatchForm("UPDATE"));
        rackView.getBtnBatchDelete().addActionListener(e -> openBatchForm("DELETE"));
        rackView.getBtnBatchMove().addActionListener(e -> openBatchForm("MOVE"));
    }

    public void loadVisualRack(String rackId) {
        rackView.renderRackSlots(currentRack);
        rackView.setVisible(true);
    }

    public void openBatchForm(String operationType) {
        batchFormView = new BatchServerFormView(currentRack.getAvailableU(), operationType);
        batchFormView.getBtnExecute().addActionListener(e -> {
            List<Server> dataInput = batchFormView.getBatchData();
            String target = batchFormView.getSelectedTargetRack();
            if (dataInput != null && !dataInput.isEmpty()) {
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

        task.start();
    }
}
