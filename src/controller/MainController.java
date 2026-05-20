/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package controller;

import dao.RoomDAO;
import dao.RackDAO;
import model.DataCenterRoom;
import view.BaseView;
import view.Viewport;

import javax.swing.*;
import java.awt.Color;
import java.util.List;

/**
 *
 * @author mahar
 */
public class MainController {
    private BaseView mainView;
    private DataCenterRoom dataCenterModel;

    public MainController(BaseView mainView, DataCenterRoom dataCenterModel) {
        this.mainView = mainView;
        this.dataCenterModel = dataCenterModel;
    }

    public void initController() {
        mainView.updateViewportRoom(dataCenterModel);
        mainView.setDashboardTitle(dataCenterModel.getRoomName());

        mainView.getItemNavDashboard().addActionListener(e -> {
            mainView.navToDashboard();
            mainView.updateViewportRoom(dataCenterModel);
        });

        mainView.getItemNavMap().addActionListener(e -> {
            mainView.navToMap();
        });

        mainView.getBtnSelect().addActionListener(e -> {
            mainView.getViewport().setMode(Viewport.Mode.SELECT);
            highlightActiveButton(mainView.getBtnSelect());
        });

        mainView.getBtnBuildRack().addActionListener(e -> {
            mainView.getViewport().setMode(Viewport.Mode.BUILD_RACK);
            highlightActiveButton(mainView.getBtnBuildRack());
        });

        mainView.getBtnBuildPath().addActionListener(e -> {
            mainView.getViewport().setMode(Viewport.Mode.BUILD_PATH);
            highlightActiveButton(mainView.getBtnBuildPath());
        });
        mainView.getBtnDemolish().addActionListener(e -> {
            mainView.getViewport().setMode(Viewport.Mode.DEMOLISH);
            highlightActiveButton(mainView.getBtnDemolish());
        });

        highlightActiveButton(mainView.getBtnSelect());

        mainView.getItemAddRoom().addActionListener(e -> {
            JTextField txtRoomName = new JTextField();
            JComboBox<String> cbSize = new JComboBox<>(new String[]{
                    "Small (32 x 32)",
                    "Medium (64 x 64)",
                    "Large (128 x 128)"
            });

            Object[] message = {
                    "Masukkan Nama Room Baru:", txtRoomName,
                    "Tentukan Ukuran Area:", cbSize
            };

            int option = JOptionPane.showConfirmDialog(mainView, message, "Create New Room", JOptionPane.OK_CANCEL_OPTION);
            if (option == JOptionPane.OK_OPTION) {
                String newRoomName = txtRoomName.getText().trim();
                if (!newRoomName.isEmpty()) {
                    int size = 64;
                    if (cbSize.getSelectedIndex() == 0) size = 32;
                    else if (cbSize.getSelectedIndex() == 2) size = 128;

                    RoomDAO roomDAO = new RoomDAO();
                    if (roomDAO.createRoom(newRoomName, size, size)) {
                        JOptionPane.showMessageDialog(mainView, "Room '" + newRoomName + "' berhasil dibuat dengan ukuran " + size + "x" + size);
                    } else {
                        JOptionPane.showMessageDialog(mainView, "Gagal membuat room. Nama mungkin sudah ada.");
                    }
                }
            }
        });

        mainView.getItemChangeLocation().addActionListener(e -> {
            RoomDAO roomDAO = new RoomDAO();
            List<String> roomsList = roomDAO.getAllRoomNames();
            String[] availableRooms = roomsList.toArray(new String[0]);

            String selectedRoom = (String) JOptionPane.showInputDialog(
                    mainView,
                    "Pilih Lokasi Ruangan:",
                    "Change Location",
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    availableRooms,
                    availableRooms.length > 0 ? availableRooms[0] : null
            );

            if (selectedRoom != null) {
                dataCenterModel = roomDAO.getRoomByName(selectedRoom);

                RackDAO rackDAO = new RackDAO();
                for (model.ServerRack rack : rackDAO.getRacksByRoom(selectedRoom)) {
                    dataCenterModel.addRack(rack);
                }

                dao.PathDAO pathDAO = new dao.PathDAO();
                for (model.RoomPath p : pathDAO.getPathsByRoom(selectedRoom)) {
                    dataCenterModel.addPath(p);
                }

                mainView.setDashboardTitle(selectedRoom);
                mainView.updateViewportRoom(dataCenterModel);
                JOptionPane.showMessageDialog(mainView, "Berhasil memuat lokasi: " + selectedRoom);
            }
        });

        mainView.setVisible(true);
    }

    private void highlightActiveButton(JButton activeBtn) {
        mainView.getBtnSelect().setBackground(Color.decode("#4F545C"));
        mainView.getBtnBuildRack().setBackground(Color.decode("#4F545C"));
        mainView.getBtnBuildPath().setBackground(Color.decode("#4F545C"));
        mainView.getBtnDemolish().setBackground(Color.decode("#C0392B"));

        activeBtn.setBackground(Color.decode("#F3C623"));
        activeBtn.setForeground(Color.BLACK);

        if(activeBtn != mainView.getBtnSelect()) mainView.getBtnSelect().setForeground(Color.WHITE);
        if(activeBtn != mainView.getBtnBuildRack()) mainView.getBtnBuildRack().setForeground(Color.WHITE);
        if(activeBtn != mainView.getBtnBuildPath()) mainView.getBtnBuildPath().setForeground(Color.WHITE);
        if(activeBtn != mainView.getBtnDemolish()) mainView.getBtnDemolish().setForeground(Color.WHITE);
    }
}