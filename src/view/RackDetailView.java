/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package view;

import model.ServerRack;

import javax.swing.*;
import java.awt.*;

/**
 *
 * @author mahar
 */
public class RackDetailView extends JDialog {
    private JPanel slotsVisualPanel;
    private JButton btnBatchAdd;
    private JButton btnBatchDelete;
    private JButton btnBatchUpdate;
    private JButton btnBatchMove;

    public RackDetailView() {
        setTitle("Rack Detail");
        setSize(500, 600);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel topPanel = new JPanel();
        btnBatchAdd = new JButton("Batch Add");
        btnBatchUpdate = new JButton("Batch Update");
        btnBatchMove = new JButton("Batch Move");
        btnBatchDelete = new JButton("Batch Delete");

        topPanel.add(btnBatchAdd);
        topPanel.add(btnBatchUpdate);
        topPanel.add(btnBatchMove);
        topPanel.add(btnBatchDelete);

        slotsVisualPanel = new JPanel();
        slotsVisualPanel.setLayout(new BoxLayout(slotsVisualPanel, BoxLayout.Y_AXIS));
        JScrollPane scrollPane = new JScrollPane(slotsVisualPanel);
        add(scrollPane, BorderLayout.CENTER);
    }

    public void renderRackSlots(ServerRack rack) {
        slotsVisualPanel.removeAll();
        setTitle("Detail Rack: " + rack.getRackId());

        String[] slotData = rack.getVisualRepresentation();

        for (int i = slotData.length - 1; i >= 0; i--) {
            JLabel slotLabel = new JLabel(slotData[i]);
            slotLabel.setBorder(BorderFactory.createLineBorder(Color.GRAY));
            slotLabel.setOpaque(true);

            if (slotData[i].contains("EMPTY")) {
                slotLabel.setBackground(Color.GREEN);
            } else {
                slotLabel.setBackground(Color.RED);
            }
            slotLabel.setPreferredSize(new Dimension(450, 30));
            slotsVisualPanel.add(slotLabel);
        }
        slotsVisualPanel.revalidate();
        slotsVisualPanel.repaint();
    }

    public JButton getBtnBatchAdd() {
        return btnBatchAdd;
    }

    public JButton getBtnBatchDelete() {
        return btnBatchDelete;
    }

    public JButton getBtnBatchUpdate() {
        return btnBatchUpdate;
    }

    public JButton getBtnBatchMove() {
        return btnBatchMove;
    }
}
