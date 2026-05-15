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


    public RackDetailView() {
        setTitle("Rack Detail");
        setSize(600, 600);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel topPanel = new JPanel();
        btnBatchAdd = new JButton("Add");
        btnBatchUpdate = new JButton("Update");
        btnBatchMove = new JButton("Move");
        btnBatchDelete = new JButton("Delete");
        btnShowDetail = new JButton("Server Detail");

        topPanel.add(btnBatchAdd);
        topPanel.add(btnBatchUpdate);
        topPanel.add(btnBatchMove);
        topPanel.add(btnBatchDelete);
        topPanel.add(btnShowDetail);

        add(topPanel, BorderLayout.NORTH);

        slotsVisualPanel = new JPanel();
        slotsVisualPanel.setLayout(new BoxLayout(slotsVisualPanel, BoxLayout.Y_AXIS));
        JScrollPane scrollPane = new JScrollPane(slotsVisualPanel);

        add(scrollPane, BorderLayout.CENTER);
    }

    public void renderRackSlots(ServerRack rack) {
        slotsVisualPanel.removeAll();
        setTitle("Detail Rack: " + rack.getRackId());

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
            btnBatchAdd.setVisible(true);
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
