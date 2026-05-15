/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package view;

import model.Server;

import javax.swing.*;
import java.awt.*;

/**
 *
 * @author mahar
 */
public class ServerDetailView extends JPanel {
    private JComboBox<String> cbStatus;
    private JTextField txtInputCpu;
    private JTextField txtInputRam;
    private JButton btnSetUtil;

    private JProgressBar pbCpuUtilization;
    private JProgressBar pbRamUtilization;
    private JProgressBar pbStorage;

    private JLabel lblServerId;

    public ServerDetailView() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(BorderFactory.createTitledBorder("Detail & Monitoring Server"));

        lblServerId = new JLabel("ID: - | Model: -");
        pbCpuUtilization = createCustomBar("CPU");
        pbRamUtilization = createCustomBar("RAM");
        pbStorage = createCustomBar("Storage");

        JPanel formPanel = new JPanel(new GridLayout(2, 4, 5, 5));
        cbStatus = new JComboBox<>(new String[]{"Online", "Offline", "Maintenance"});
        txtInputCpu = new JTextField(5);
        txtInputRam = new JTextField(5);
        btnSetUtil = new JButton("Simpan Perubahan");

        formPanel.add(new JLabel("Status:")); formPanel.add(cbStatus);
        formPanel.add(new JLabel("Set CPU (%):")); formPanel.add(txtInputCpu);
        formPanel.add(new JLabel("Set RAM (%):")); formPanel.add(txtInputRam);
        formPanel.add(btnSetUtil);

        add(lblServerId);
        add(Box.createVerticalStrut(10));
        add(pbCpuUtilization);
        add(Box.createVerticalStrut(5));
        add(pbRamUtilization);
        add(Box.createVerticalStrut(5));
        add(pbStorage);
        add(Box.createVerticalStrut(20));
        add(formPanel);
    }

    private JProgressBar createCustomBar(String title) {
        JProgressBar pb = new JProgressBar(0, 100);
        pb.setStringPainted(true);
        pb.setBorder(BorderFactory.createTitledBorder(title));
        return pb;
    }

    public void displayServerStats(Server s) {
        if(s == null) return;
        lblServerId.setText("ID: " + s.getIdAsset() + " | Model: " + s.getModelName());
        cbStatus.setSelectedItem(s.getStatus());

        updateCpuBar(s.getCpuUtilization());
        updateRamBar(s.getRamUtilization());
        updateStorageBar(s.getUsedStorageGB(), s.getTotalStorageGB());
    }

    public void updateCpuBar(double util) {
        pbCpuUtilization.setValue((int) util);
        pbCpuUtilization.setString(String.format("%.1f %%", util));
    }

    public void updateRamBar(double util) {
        pbRamUtilization.setValue((int) util);
        pbRamUtilization.setString(String.format("%.1f %%", util));
    }

    public void updateStorageBar(int used, int total) {
        int percentage = (int) (((double) used / total) * 100);
        pbStorage.setValue(percentage);
        pbStorage.setString(used + "GB / " + total + "GB (" + percentage + "%)");
    }

    public JButton getBtnSetUtil() {
        return btnSetUtil;
    }

    public String getInputCpu() {
        return txtInputCpu.getText();
    }

    public String getInputRam() {
        return txtInputRam.getText();
    }

    public String getSelectedStatus() {
        return cbStatus.getSelectedItem().toString();
    }
}
