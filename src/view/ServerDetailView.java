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
    private JTextField txtInputStorage;
    private JButton btnSetUtil;

    private JProgressBar pbCpuUtilization;
    private JProgressBar pbRamUtilization;
    private JProgressBar pbStorage;

    private JLabel lblServerId;
    private JLabel lblCpuName;
    private JLabel lblOsType;

    public ServerDetailView() {
        setLayout(new BorderLayout());

        JTabbedPane tabbedPane = new JTabbedPane();

        // tab 1
        JPanel detailPanel = new JPanel();
        detailPanel.setLayout(new BoxLayout(detailPanel, BoxLayout.Y_AXIS));
        detailPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        lblServerId = new JLabel("ID: - | Model: -");
        lblCpuName = new JLabel("CPU Name: - | Cores: -");
        lblOsType = new JLabel("OS: -");
        
        lblServerId.setAlignmentX(Component.LEFT_ALIGNMENT);
        lblCpuName.setAlignmentX(Component.LEFT_ALIGNMENT);
        lblOsType.setAlignmentX(Component.LEFT_ALIGNMENT);

        pbCpuUtilization = createCustomBar("CPU Utilization");
        pbRamUtilization = createCustomBar("RAM Utilization");
        pbStorage = createCustomBar("Storage Usage");

        detailPanel.add(lblServerId);
        detailPanel.add(Box.createVerticalStrut(5));
        detailPanel.add(lblCpuName);
        detailPanel.add(Box.createVerticalStrut(5));
        detailPanel.add(lblOsType);
        detailPanel.add(Box.createVerticalStrut(15));
        detailPanel.add(pbCpuUtilization);
        detailPanel.add(Box.createVerticalStrut(5));
        detailPanel.add(pbRamUtilization);
        detailPanel.add(Box.createVerticalStrut(5));
        detailPanel.add(pbStorage);

        // tab 2
        JPanel editPanel = new JPanel(new GridLayout(4, 2, 5, 10));
        editPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        cbStatus = new JComboBox<>(new String[]{"Online", "Offline", "Maintenance"});
        txtInputCpu = new JTextField(5);
        txtInputRam = new JTextField(5);
        txtInputStorage = new JTextField(5);
        btnSetUtil = new JButton("Perbarui Status");

        editPanel.add(new JLabel("Set Status:")); editPanel.add(cbStatus);
        editPanel.add(new JLabel("Set CPU (%):")); editPanel.add(txtInputCpu);
        editPanel.add(new JLabel("Set RAM (%):")); editPanel.add(txtInputRam);
        editPanel.add(new JLabel("Set Storage (GB):")); editPanel.add(txtInputStorage);
        
        JPanel bottomBtnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottomBtnPanel.add(btnSetUtil);

        JPanel editWrapper = new JPanel(new BorderLayout());
        editWrapper.add(editPanel, BorderLayout.NORTH);
        editWrapper.add(bottomBtnPanel, BorderLayout.SOUTH);

        tabbedPane.addTab("Detail Server", detailPanel);
        tabbedPane.addTab("Edit Usage", editWrapper);

        add(tabbedPane, BorderLayout.CENTER);
    }

    private JProgressBar createCustomBar(String title) {
        JProgressBar pb = new JProgressBar(0, 100);
        pb.setStringPainted(true);
        pb.setBorder(BorderFactory.createTitledBorder(title));
        pb.setAlignmentX(Component.LEFT_ALIGNMENT);
        return pb;
    }

    public void displayServerStats(Server s) {
        if(s == null) return;
        lblServerId.setText("ID: " + s.getIdAsset() + " | Model: " + s.getModelName());
        lblCpuName.setText("CPU Name: " + (s.getCpuName() != null ? s.getCpuName() : "Unknown") + " | Cores: " + s.getCpuCores() + " | RAM: " + s.getTotalRamGB() + " GB");
        lblOsType.setText("OS: " + s.getOsType() + " | Storage: " + s.getTotalStorageGB() + " GB");
        
        cbStatus.setSelectedItem(s.getStatus());
        txtInputCpu.setText(String.valueOf(s.getCpuUtilization()));
        txtInputRam.setText(String.valueOf(s.getRamUtilization()));
        txtInputStorage.setText(String.valueOf(s.getUsedStorageGB()));

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
        if (total <= 0) total = 1;
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

    public String getInputStorage() {
        return txtInputStorage.getText();
    }

    public String getSelectedStatus() {
        return cbStatus.getSelectedItem().toString();
    }
}
