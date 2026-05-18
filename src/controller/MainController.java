/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import dao.ServerDAO;
import model.DataCenterRoom;
import model.GridLocation;
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

        // --- EVENT LISTENER UNTUK MENU BAR ---

        // 1. Menu Tools -> Add Room
        mainView.getItemAddRoom().addActionListener(e -> {
            String newRoomName = JOptionPane.showInputDialog(mainView, "Masukkan Nama Room Baru:");
            if (newRoomName != null && !newRoomName.trim().isEmpty()) {
                dao.RoomDAO roomDAO = new dao.RoomDAO();
                if (roomDAO.createRoom(newRoomName)) {
                    JOptionPane.showMessageDialog(mainView, "Room '" + newRoomName + "' berhasil ditambahkan ke Database!");
                } else {
                    JOptionPane.showMessageDialog(mainView, "Gagal menambah room, pastikan nama room unik.");
                }
            }
        });

        // 2. Menu Home -> Change Location
        mainView.getItemChangeLocation().addActionListener(e -> {
            dao.RoomDAO roomDAO = new dao.RoomDAO();
            java.util.List<String> roomsList = roomDAO.getAllRoomNames();
            String[] availableRooms = roomsList.toArray(new String[0]); // Konversi ke array

            String selectedRoom = (String) JOptionPane.showInputDialog(
                    mainView,
                    "Pilih Lokasi Room Data Center:",
                    "Change Location",
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    availableRooms,
                    availableRooms.length > 0 ? availableRooms[0] : null
            );

            if (selectedRoom != null) {
                // Update model room
                dataCenterModel = new model.DataCenterRoom(selectedRoom);

                // Ambil ulang data rak KHUSUS untuk room yang dipilih
                dao.RackDAO rackDAO = new dao.RackDAO();
                for (model.ServerRack rack : rackDAO.getRacksByRoom(selectedRoom)) {
                    dataCenterModel.addRack(rack);
                }

                mainView.setDashboardTitle(selectedRoom);
                loadGridRacks();
                JOptionPane.showMessageDialog(mainView, "Berhasil memuat lokasi: " + selectedRoom);
            }
        });

        mainView.setVisible(true);
    }

    public void loadGridRacks() {
        JPanel grid = mainView.getGridPanel();
        grid.removeAll();

        List<ServerRack> racks = dataCenterModel.getAllRacks();

        for (int r = 0; r < 5; r++) {
            for (int c = 0; c < 8; c++) {
                if (r == 2 || c == 2 || c == 5) {
                    grid.add(new JLabel(""));
                } else {
                    ServerRack foundRack = null;
                    for (ServerRack rack : racks) {
                        if (rack.getLocation().getxCoordinate() == c && rack.getLocation().getyCoordinate() == r) {
                            foundRack = rack;
                            break;
                        }
                    }

                    if (foundRack != null) {
                        JButton btnRack = new JButton();
                        btnRack.setLayout(new BorderLayout());

                        JLabel lblRackId = new JLabel(foundRack.getRackId(), SwingConstants.CENTER);
                        JLabel lblZone = new JLabel(foundRack.getLocation().getLocationString(), SwingConstants.CENTER);

                        btnRack.add(lblRackId, BorderLayout.CENTER);
                        btnRack.add(lblZone, BorderLayout.SOUTH);
                        btnRack.setBackground(Color.decode("#c5cae9"));

                        final String finalRackId = foundRack.getRackId();

                        btnRack.addActionListener(e -> onRackClicked(finalRackId));
                        grid.add(btnRack);;
                    } else {
                        JButton btnEmpty = new JButton("Insert Rack");
                        btnEmpty.setBorder(BorderFactory.createDashedBorder(Color.GRAY, 2, 5, 2, false));
                        btnEmpty.setBackground(Color.decode("#f5f5f5"));
                        btnEmpty.setForeground(Color.GRAY);

                        final int xCoord = c;
                        final int yCoord = r;

                        btnEmpty.addActionListener(e -> onEmptySlotClicked(xCoord, yCoord));

                        grid.add(btnEmpty);
                    }
                }
            }
        }
        grid.revalidate();
        grid.repaint();
    }

    public void onEmptySlotClicked(int x, int y) {
        JTextField txtRackId = new JTextField();
        JTextField txtMaxCapacity = new JTextField("42");
        JTextField txtZone = new JTextField("ZONA A");

        Object[] message = {
                "Masukkan ID Rak (Misal: RACK-A1):", txtRackId,
                "Kapasitas Slot (U):", txtMaxCapacity,
                "Nama Zona Letak:", txtZone
        };

        int option = JOptionPane.showConfirmDialog(mainView, message, "Tambah Server Rack Baru", JOptionPane.OK_CANCEL_OPTION);

        if (option == JOptionPane.OK_OPTION) {
            try {
                String id = txtRackId.getText();
                int capacity = Integer.parseInt(txtMaxCapacity.getText());
                String zone = txtZone.getText();

                GridLocation loc = new GridLocation(x, y, zone);
                ServerRack newRack = new ServerRack(id, capacity, loc);

                dao.RackDAO rackDAO = new dao.RackDAO();
                boolean isSaved = rackDAO.create(newRack, dataCenterModel.getRoomName());

                if (isSaved) {
                    dataCenterModel.addRack(newRack);
                    loadGridRacks();
                    JOptionPane.showMessageDialog(mainView, "Rak berhasil ditambahkan ke Database!");
                } else {
                    JOptionPane.showMessageDialog(mainView, "Gagal menyimpan ke Database (ID mungkin duplikat).");
                }

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(mainView, "Error: Kapasitas harus berupa angka valid!");
            }
        }
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
