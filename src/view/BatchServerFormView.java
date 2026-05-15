/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package view;

import model.Server;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author mahar
 */
public class BatchServerFormView extends JDialog {
    private JPanel dynamicFormPanel;
    private JButton btnAddRow;
    private JButton btnExecute;
    private JComboBox<String> cbTargetRack;
    private int maxAllowedCapacityU;
    private int currentFormU;

    private List<ServerRowUI> rowUIs;

    private int nextServerId;

    public BatchServerFormView(int maxCapacityU, String operationType, List<String> availableRacks) {
        this.maxAllowedCapacityU = maxCapacityU;
        this.currentFormU = 0;
        this.rowUIs = new ArrayList<>();
        this.nextServerId = new dao.ServerDAO().getNextServerId();

        setTitle("Batch Operation:  " + operationType);
        setSize(800, 400);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        btnAddRow = new JButton("+ Tambah Baris Server");
        btnExecute = new JButton("Eksekusi Batch");

        btnAddRow.setVisible(operationType.equalsIgnoreCase("ADD"));

        cbTargetRack = new JComboBox<>(availableRacks.toArray(new String[0]));
        cbTargetRack.setVisible(operationType.equalsIgnoreCase("MOVE"));

        headerPanel.add(btnAddRow);
        headerPanel.add(btnExecute);
        headerPanel.add(cbTargetRack);
        add(headerPanel, BorderLayout.NORTH);

        dynamicFormPanel = new JPanel();
        dynamicFormPanel.setLayout(new BoxLayout(dynamicFormPanel, BoxLayout.Y_AXIS));
        JScrollPane scrollPane = new JScrollPane(dynamicFormPanel);
        add(scrollPane, BorderLayout.CENTER);

        btnAddRow.addActionListener(e -> addFormRow(null));

    }

    public void addFormRow(Server existingServer) {
        if (!validateCapacity(1)) {
            JOptionPane.showMessageDialog(this, "Kapasitas rak tidak mencukupi (Sisa " +
                    (maxAllowedCapacityU - currentFormU) + "U)!");
            return;
        }

        ServerRowUI newRow = new ServerRowUI(existingServer);
        rowUIs.add(newRow);
        dynamicFormPanel.add(newRow.panel);

        currentFormU += 1;

        dynamicFormPanel.revalidate();
        dynamicFormPanel.repaint();
    }

    public void removeFormRow(JPanel rowPanel, ServerRowUI uiReference) {
        dynamicFormPanel.remove(rowPanel);
        rowUIs.remove(uiReference);
        currentFormU -= 1;
        dynamicFormPanel.revalidate();
        dynamicFormPanel.repaint();
    }

    public boolean validateCapacity(int requiredU) {
        return (currentFormU + requiredU) <= maxAllowedCapacityU;
    }

    public List<Server> getBatchData() {
        List<Server> batchList = new ArrayList<>();
        try {
            for (ServerRowUI row : rowUIs) {
                String id = row.txtId.getText();
                String model = row.txtModel.getText();
                String cpuName = row.txtCpuName.getText();
                int cpuCores = Integer.parseInt(row.txtCpu.getText());
                int ram = Integer.parseInt(row.txtRam.getText());
                int disk = Integer.parseInt(row.txtDisk.getText());

                Server s = new Server(id, model, 1, "Offline", cpuName, cpuCores, ram, "Linux", disk, 0);
                batchList.add(s);
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Pastikan nilai CPU Cores, RAM, & Disk berupa Angka!");
            return null;
        }
        return batchList;
    }

    public String getSelectedTargetRack() {
        return cbTargetRack.getSelectedItem() != null ? cbTargetRack.getSelectedItem().toString() : "";
    }

    public JButton getBtnExecute() {
        return btnExecute;
    }

    private class ServerRowUI {
        JPanel panel;
        JTextField txtId, txtModel, txtCpuName, txtCpu, txtRam, txtDisk;
        JButton btnDeleteRow;

        public ServerRowUI(Server existingServer) {
            panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            panel.setBorder(BorderFactory.createEtchedBorder());

            String initialId = existingServer != null ? existingServer.getIdAsset() : "SRV-" + (nextServerId++);
            String initialModel = existingServer != null ? existingServer.getModelName() : "Dell PowerEdge";
            String initialCpuName = (existingServer != null && existingServer.getCpuName() != null) ? existingServer.getCpuName() : "Intel Xeon";
            String initialCpu = existingServer != null ? String.valueOf(existingServer.getCpuCores()) : "64";
            String initialRam = existingServer != null ? String.valueOf(existingServer.getTotalRamGB()) : "128";
            String initialDisk = existingServer != null ? String.valueOf(existingServer.getTotalStorageGB()) : "1024";

            txtId = new JTextField(initialId, 8);
            txtId.setEditable(false);
            txtModel = new JTextField(initialModel, 10);
            txtCpuName = new JTextField(initialCpuName, 10);
            txtCpu = new JTextField(initialCpu, 3);
            txtRam = new JTextField(initialRam, 3);
            txtDisk = new JTextField(initialDisk, 4);

            btnDeleteRow = new JButton("X");

            btnDeleteRow.addActionListener(e -> removeFormRow(panel, this));

            panel.add(new JLabel("ID:")); panel.add(txtId);
            panel.add(new JLabel("Model:")); panel.add(txtModel);
            panel.add(new JLabel("CPU:")); panel.add(txtCpuName);
            panel.add(new JLabel("Cores:")); panel.add(txtCpu);
            panel.add(new JLabel("RAM (GB):")); panel.add(txtRam);
            panel.add(new JLabel("Disk (GB):")); panel.add(txtDisk);
            panel.add(btnDeleteRow);
        }
    }
}
