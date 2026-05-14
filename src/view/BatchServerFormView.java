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

    public BatchServerFormView(int maxCapactityU, String operationType) {
        this.maxAllowedCapacityU = maxCapactityU;
        this.currentFormU = 0;
        this.rowUIs = new ArrayList<>();

        setTitle("Batch Operation:  " + operationType);
        setSize(800, 400);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        btnAddRow = new JButton("+ Tambah Baris Server");
        btnExecute = new JButton("Eksekusi Batch");

        cbTargetRack = new JComboBox<>(new String[]{"RACK-A1", "RACK-A2", "RACK-B1"});
        cbTargetRack.setVisible(operationType.equalsIgnoreCase("MOVE"));

        headerPanel.add(btnAddRow);
        headerPanel.add(btnExecute);
        headerPanel.add(cbTargetRack);
        add(headerPanel, BorderLayout.NORTH);

        dynamicFormPanel = new JPanel();
        dynamicFormPanel.setLayout(new BoxLayout(dynamicFormPanel, BoxLayout.Y_AXIS));
        JScrollPane scrollPane = new JScrollPane(dynamicFormPanel);
        add(scrollPane, BorderLayout.CENTER);

        btnAddRow.addActionListener(e -> addFormRow());

        addFormRow();
    }

    public void addFormRow() {
        if (!validateCapacity(2)) {
            JOptionPane.showMessageDialog(this, "Kapasitas rak tidak mencukupi (Sisa " +
                    (maxAllowedCapacityU - currentFormU) + "U)!");
            return;
        }

        ServerRowUI newRow = new ServerRowUI();
        rowUIs.add(newRow);
        dynamicFormPanel.add(newRow.panel);

        currentFormU += 2;

        dynamicFormPanel.revalidate();
        dynamicFormPanel.repaint();
    }

    public void removeFormRow(JPanel rowPanel, ServerRowUI uiReference) {
        dynamicFormPanel.remove(rowPanel);
        rowUIs.remove(uiReference);
        currentFormU -= 2;
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
                int cpu = Integer.parseInt(row.txtCpu.getText());
                int ram = Integer.parseInt(row.txtRam.getText());
                int disk = Integer.parseInt(row.txtDisk.getText());

                // Menerapkan abstraksi/polimorfisme: Server sebagai ComputingDevice/HardwareEquipment
                Server s = new Server(id, model, 2, "Offline", cpu, ram, "Linux", disk, 0);
                batchList.add(s);
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Pastikan nilai CPU, RAM, & Disk berupa Angka!");
            return null;
        }
        return batchList;
    }

    public String getSelectedTargetRack() {
        return cbTargetRack.getSelectedItem() != null ? cbTargetRack.getSelectedItem().toString() : "";
    }

    public JButton getBtnExcute() {
        return btnExecute;
    }

    private class ServerRowUI {
        JPanel panel;
        JTextField txtId, txtModel, txtCpu, txtRam, txtDisk;
        JButton btnDeleteRow;

        public ServerRowUI() {
            panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            panel.setBorder(BorderFactory.createEtchedBorder());

            txtId = new JTextField("PLACEHOLDER", 1);
            txtModel = new JTextField("PLACEHOLDER", 1);
            txtCpu = new JTextField("64", 3);
            txtRam = new JTextField("128", 3);
            txtDisk = new JTextField("1024", 4);

            btnDeleteRow.addActionListener(e -> removeFormRow(panel, this));

            panel.add(new JLabel("ID:")); panel.add(txtId);
            panel.add(new JLabel("Model:")); panel.add(txtModel);
            panel.add(new JLabel("CPU (Cores):")); panel.add(txtCpu);
            panel.add(new JLabel("RAM (GB):")); panel.add(txtRam);
            panel.add(new JLabel("Disk (GB):")); panel.add(txtDisk);
            panel.add(btnDeleteRow);
        }
    }
}
