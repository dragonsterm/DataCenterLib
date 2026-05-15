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
    private JButton btnShowDetail;
    private JToggleButton[] slotButtons;
    
    private JLabel lblRackInfo;
    private JLabel lblRackZone;
    private JLabel lblInstruction;
    private JPanel actionPanel;


    public RackDetailView() {
        setTitle("Rack Detail");
        setSize(600, 650);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        //  panel top
        JPanel topInfoPanel = new JPanel();
        topInfoPanel.setLayout(new BoxLayout(topInfoPanel, BoxLayout.Y_AXIS));
        topInfoPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        lblRackInfo = new JLabel("Rack ID: - | Capacity: - U");
        lblRackZone = new JLabel("Location Zone: -");
        lblRackInfo.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblRackZone.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblRackInfo.setFont(new Font("Arial", Font.BOLD, 14));
        
        topInfoPanel.add(lblRackInfo);
        topInfoPanel.add(Box.createVerticalStrut(5));
        topInfoPanel.add(lblRackZone);
        add(topInfoPanel, BorderLayout.NORTH);

        // Panel bottom
        actionPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        lblInstruction = new JLabel("Please click one or more slots/servers for actions.");
        lblInstruction.setFont(new Font("Arial", Font.ITALIC, 13));
        
        btnBatchAdd = new JButton("Batch Add");
        btnBatchUpdate = new JButton("Batch Update");
        btnBatchMove = new JButton("Batch Move");
        btnBatchDelete = new JButton("Batch Delete");
        btnShowDetail = new JButton("Server Detail");

        actionPanel.add(lblInstruction);
        actionPanel.add(btnBatchAdd);
        actionPanel.add(btnBatchUpdate);
        actionPanel.add(btnBatchMove);
        actionPanel.add(btnBatchDelete);
        actionPanel.add(btnShowDetail);

        add(actionPanel, BorderLayout.SOUTH);

        slotsVisualPanel = new JPanel();
        slotsVisualPanel.setLayout(new BoxLayout(slotsVisualPanel, BoxLayout.Y_AXIS));
        JScrollPane scrollPane = new JScrollPane(slotsVisualPanel);

        add(scrollPane, BorderLayout.CENTER);
    }

    public void renderRackSlots(ServerRack rack) {
        slotsVisualPanel.removeAll();
        setTitle("Detail Rack: " + rack.getRackId());

        lblRackInfo.setText("Rack Name: " + rack.getRackId() + "  |  Capacity: " + rack.getMaxCapacityU() + " U");
        if (rack.getLocation() != null) {
            lblRackZone.setText("Zone: " + rack.getLocation().getZoneName() + 
                                " - Coordinates: (X: " + rack.getLocation().getxCoordinate() + 
                                ", Y: " + rack.getLocation().getyCoordinate() + ")");
        }

        String[] slotData = rack.getVisualRepresentation();
        slotButtons = new JToggleButton[slotData.length];

        for (int i = slotData.length - 1; i >= 0; i--) {
            JToggleButton slotBtn = new JToggleButton(slotData[i]);
            slotBtn.setPreferredSize(new Dimension(450, 30));

            if (slotData[i].contains("EMPTY")) {
                slotBtn.setBackground(Color.decode("#e8f5e9"));
                slotBtn.setForeground(Color.BLACK);
                slotBtn.putClientProperty("isEmpty", true);
            } else {
                slotBtn.setBackground(Color.decode("#ffcdd2"));
                slotBtn.setForeground(Color.RED);
                slotBtn.putClientProperty("isEmpty", false);
            }

            slotBtn.addActionListener(e -> fireSelectionChanged());

            slotButtons[i] = slotBtn;
            slotsVisualPanel.add(slotBtn);
        }

        fireSelectionChanged();
        
        slotsVisualPanel.revalidate();
        slotsVisualPanel.repaint();
    }

    public java.util.List<Integer> getSelectedEmptySlots() {
        java.util.List<Integer> selectedIndexes = new java.util.ArrayList<>();
        for (int i = 0; i < slotButtons.length; i++) {
            if (slotButtons[i] != null && slotButtons[i].isSelected() && 
               Boolean.TRUE.equals(slotButtons[i].getClientProperty("isEmpty"))) {
                selectedIndexes.add(i);
            }
        }
        return selectedIndexes;
    }

    public java.util.List<Integer> getSelectedOccupiedSlots() {
        java.util.List<Integer> selectedIndexes = new java.util.ArrayList<>();
        for (int i = 0; i < slotButtons.length; i++) {
            if (slotButtons[i] != null && slotButtons[i].isSelected() && 
               Boolean.FALSE.equals(slotButtons[i].getClientProperty("isEmpty"))) {
                selectedIndexes.add(i);
            }
        }
        return selectedIndexes;
    }

    private void fireSelectionChanged() {
        java.util.List<Integer> emptySelected = getSelectedEmptySlots();
        java.util.List<Integer> occupiedSelected = getSelectedOccupiedSlots();
        
        boolean hasEmptySelected = !emptySelected.isEmpty();
        boolean hasOccupiedSelected = !occupiedSelected.isEmpty();
        
        lblInstruction.setVisible(false);

        if (hasOccupiedSelected && !hasEmptySelected) {
            btnBatchAdd.setVisible(false);
            btnBatchUpdate.setVisible(true);
            btnBatchMove.setVisible(true);
            btnBatchDelete.setVisible(true);
            btnShowDetail.setVisible(occupiedSelected.size() == 1);
        } else if (hasEmptySelected && !hasOccupiedSelected) {
            btnBatchAdd.setVisible(true);
            btnBatchUpdate.setVisible(false);
            btnBatchMove.setVisible(false);
            btnBatchDelete.setVisible(false);
            btnShowDetail.setVisible(false);
        } else {
            lblInstruction.setVisible(true);
            btnBatchAdd.setVisible(false);
            btnBatchUpdate.setVisible(false);
            btnBatchMove.setVisible(false);
            btnBatchDelete.setVisible(false);
            btnShowDetail.setVisible(false);
        }
    }

    public JToggleButton[] getSlotButtons() {
        return slotButtons;
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

    public JButton getBtnShowDetail() {
        return btnShowDetail;
    }
}
