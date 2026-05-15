/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import dao.ServerDAO;
import model.DataCenterRoom;
import model.ServerRack;
import view.MainDashboardView;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 *
 * @author mahar
 */
public class MainController {
    private MainDashboardView mainView;
    private DataCenterRoom dataCenterModel;

    public MainController(MainDashboardView mainView, DataCenterRoom dataCenterModel) {
        this.mainView = mainView;
        this.dataCenterModel = dataCenterModel;
    }

    public void initController() {
        loadGridRacks();
        mainView.setVisible(true);
    }

    public void loadGridRacks() {
        List<ServerRack> racks = dataCenterModel.getAllRacks();
        mainView.renderGrid(racks);

        JPanel grid = mainView.getGridPanel();

        for (ServerRack rack : racks) {
            JButton btnRack = new JButton();
            btnRack.setLayout(new BorderLayout());

            JLabel lblRackId = new JLabel(rack.getRackId(), SwingConstants.CENTER);
            JLabel lblZone = new JLabel(rack.getLocation().getLocationString(), SwingConstants.CENTER);

            btnRack.add(lblRackId, BorderLayout.CENTER);
            btnRack.add(lblZone, BorderLayout.SOUTH);
            btnRack.setBackground(Color.decode("#c5cae9"));

            btnRack.addActionListener(e -> onRackClicked(rack.getRackId()));

            grid.add(btnRack);
        }
        grid.revalidate();
        grid.repaint();
    }

    public void onRackClicked(String rackId) {
        ServerRack clickedRack = dataCenterModel.findRackById(rackId);

        if (clickedRack != null) {
            ServerDAO serverDAO = new ServerDAO();
            RackController rackController = new RackController(dataCenterModel, clickedRack, serverDAO);
            rackController.loadVisualRack(rackId);
        }
    }
}
