/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package view;

import model.HardwareEquipment;
import model.ServerRack;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.geom.Arc2D;
import java.awt.geom.Ellipse2D;
import java.util.List;

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
    
    private Timer interactionTimer;
    private int holdProgress = 0;
    private boolean isHoldingServer = false;
    private boolean isClicking = false;
    private HardwareEquipment selectedServerToMove = null;
    private int selectedStartSlot = -1;
    private int lastMouseX, lastMouseY;
    private int holdMouseX, holdMouseY;
    
    public interface ServerMoveListener {
        void onServerMovedWithinRack(String serverId, int oldStartSlot, int targetStartSlot);
    }
    private ServerMoveListener serverMoveListener;

    public void setServerMoveListener(ServerMoveListener listener) {
        this.serverMoveListener = listener;
    }

    public RackDetailView() {
        setTitle("Rack Detail");

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int maxHeight = screenSize.height - 100;
        int preferredHeight = Math.min(700, maxHeight);

        setSize(600, preferredHeight);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

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

        actionPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        lblInstruction = new JLabel("Please click one or more slots/servers for actions.");
        lblInstruction.setFont(new Font("Arial", Font.ITALIC, 13));

        btnBatchAdd = new JButton("Add");
        btnBatchUpdate = new JButton("Update");
        btnBatchMove = new JButton("Move");
        btnBatchDelete = new JButton("Delete");
        btnShowDetail = new JButton("Server Detail");

        actionPanel.add(lblInstruction);
        actionPanel.add(btnBatchAdd);
        actionPanel.add(btnBatchUpdate);
        actionPanel.add(btnBatchMove);
        actionPanel.add(btnBatchDelete);
        actionPanel.add(btnShowDetail);

        add(actionPanel, BorderLayout.SOUTH);

        slotsVisualPanel = new JPanel() {
            @Override
            public void paint(Graphics g) {
                super.paint(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                if (isClicking && !isHoldingServer && holdProgress > 0) {
                    double radius = 18.0;
                    double cx = holdMouseX - radius;
                    double cy = holdMouseY - radius;

                    g2d.setColor(new Color(0, 0, 0, 120));
                    g2d.setStroke(new BasicStroke(5, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                    g2d.draw(new Ellipse2D.Double(cx, cy, radius * 2, radius * 2));

                    g2d.setColor(Color.WHITE);
                    double angle = 360.0 * (holdProgress / 100.0);
                    g2d.draw(new Arc2D.Double(cx, cy, radius * 2, radius * 2, 90, -angle, Arc2D.OPEN));
                } else if (isHoldingServer && selectedServerToMove != null) {
                    g2d.setColor(new Color(46, 204, 113, 150));
                    int h = 37 * selectedServerToMove.getSizeInU(); 
                    g2d.fillRect(20, lastMouseY - h/2, getWidth() - 40, h);
                    
                    g2d.setColor(Color.WHITE);
                    g2d.setStroke(new BasicStroke(2));
                    g2d.drawRect(20, lastMouseY - h/2, getWidth() - 40, h);
                }
            }
        };
        slotsVisualPanel.setLayout(new BoxLayout(slotsVisualPanel, BoxLayout.Y_AXIS));

        JScrollPane scrollPane = new JScrollPane(slotsVisualPanel);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(scrollPane, BorderLayout.CENTER);
        
        interactionTimer = new Timer(10, e -> {
            if (isClicking && !isHoldingServer) {
                if (selectedServerToMove != null) {
                    holdProgress++;
                    if (holdProgress >= 100) {
                        isHoldingServer = true;
                        holdProgress = 0;
                    }
                    slotsVisualPanel.repaint();
                }
            }
        });
        interactionTimer.setInitialDelay(250);
    }

    public void renderRackSlots(ServerRack rack) {
        slotsVisualPanel.removeAll();

        slotsVisualPanel.setBackground(Color.decode("#78909C"));
        slotsVisualPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(5, 20, 5, 20),
                BorderFactory.createLineBorder(Color.DARK_GRAY, 8)
        ));

        setTitle("Detail Rack: " + rack.getRackId());

        int usedU = rack.getMaxCapacityU() - rack.getAvailableU();

        lblRackInfo.setText("Rack Name: " + rack.getRackId() + "  |  Capacity: " + usedU + "/" + rack.getMaxCapacityU() + " Server Unit");

        if (rack.getLocation() != null) {
            lblRackZone.setText("Zone: " + rack.getLocation().getZoneName() +
                    " - Coordinates: (X: " + rack.getLocation().getxCoordinate() +
                    ", Y: " + rack.getLocation().getyCoordinate() + ")");
        }

        String[] slotData = rack.getVisualRepresentation();
        slotButtons = new JToggleButton[slotData.length];

        List<HardwareEquipment> hwList = rack.getHardwareList();

        for (int i = slotData.length - 1; i >= 0; i--) {
            final String currentSlotText = slotData[i];
            final int slotIndex = i;

            JToggleButton slotBtn = new JToggleButton(currentSlotText) {
                @Override
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    if (getClientProperty("statusColor") != null) {
                        Color c = (Color) getClientProperty("statusColor");
                        Graphics2D g2 = (Graphics2D) g.create();
                        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                        int diameter = 14;
                        int x = getWidth() - diameter - 20;
                        int y = (getHeight() - diameter) / 2;

                        g2.setColor(c);
                        g2.fillOval(x, y, diameter, diameter);

                        g2.setColor(Color.DARK_GRAY);
                        g2.drawOval(x, y, diameter, diameter);
                        g2.dispose();
                    }
                }
            };

            slotBtn.setPreferredSize(new Dimension(450, 35));
            slotBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
            slotBtn.setHorizontalAlignment(SwingConstants.LEFT);
            slotBtn.setFont(new Font("Monospaced", Font.BOLD, 13));

            if (currentSlotText.contains("EMPTY")) {
                slotBtn.setBackground(Color.decode("#ffcdd2"));
                slotBtn.setForeground(Color.DARK_GRAY);
                slotBtn.putClientProperty("isEmpty", true);
            } else {
                slotBtn.setBackground(Color.decode("#e8f5e9"));
                slotBtn.setForeground(Color.BLACK);
                slotBtn.putClientProperty("isEmpty", false);

                String id = extractId(currentSlotText);
                for (HardwareEquipment hw : hwList) {
                    if (hw.getIdAsset().equals(id)) {
                        String status = hw.getStatus();
                        if ("Online".equalsIgnoreCase(status)) {
                            slotBtn.putClientProperty("statusColor", Color.GREEN);
                        } else if ("Offline".equalsIgnoreCase(status)) {
                            slotBtn.putClientProperty("statusColor", Color.RED);
                        } else if ("Maintenance".equalsIgnoreCase(status)) {
                            slotBtn.putClientProperty("statusColor", Color.YELLOW);
                        }
                        break;
                    }
                }
            }

            slotBtn.addActionListener(e -> fireSelectionChanged());
            
            slotBtn.addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    if (!Boolean.TRUE.equals(slotBtn.getClientProperty("isEmpty"))) {
                        isClicking = true;
                        holdProgress = 0;
                        String id = extractId(currentSlotText);
                        for (HardwareEquipment hw : hwList) {
                            if (hw.getIdAsset().equals(id)) {
                                selectedServerToMove = hw;
                                break;
                            }
                        }
                        
                        int actualStartSlot = slotIndex;
                        for (int k = 0; k < slotData.length; k++) {
                            if (extractId(slotData[k]).equals(id)) {
                                actualStartSlot = k;
                                break;
                            }
                        }
                        selectedStartSlot = actualStartSlot;
                        
                        Point conv = SwingUtilities.convertPoint(slotBtn, e.getPoint(), slotsVisualPanel);
                        holdMouseX = conv.x;
                        holdMouseY = conv.y;
                        lastMouseX = conv.x;
                        lastMouseY = conv.y;
                        interactionTimer.restart();
                        slotsVisualPanel.repaint();
                    }
                }

                @Override
                public void mouseReleased(MouseEvent e) {
                    isClicking = false;
                    interactionTimer.stop();
                    if (isHoldingServer && selectedServerToMove != null) {
                        isHoldingServer = false;
                        Point p = SwingUtilities.convertPoint(slotBtn, e.getPoint(), slotsVisualPanel);
                        
                        int targetSlotIndex = -1;
                        for (int j = 0; j < slotButtons.length; j++) {
                            if (slotButtons[j] != null) {
                                Rectangle bounds = slotButtons[j].getBounds();
                                if (bounds.contains(p)) {
                                    targetSlotIndex = j;
                                    break;
                                }
                            }
                        }

                        if (targetSlotIndex != -1 && serverMoveListener != null) {
                            serverMoveListener.onServerMovedWithinRack(selectedServerToMove.getIdAsset(), selectedStartSlot, targetSlotIndex);
                        }
                        selectedServerToMove = null;
                        selectedStartSlot = -1;
                        holdProgress = 0;
                        slotsVisualPanel.repaint();
                    } else {
                        holdProgress = 0;
                        slotsVisualPanel.repaint();
                    }
                }
            });

            slotBtn.addMouseMotionListener(new MouseMotionAdapter() {
                @Override
                public void mouseDragged(MouseEvent e) {
                    Point conv = SwingUtilities.convertPoint(slotBtn, e.getPoint(), slotsVisualPanel);
                    lastMouseX = conv.x;
                    lastMouseY = conv.y;
                    
                    if (interactionTimer.isRunning() && !isHoldingServer) {
                        int distFromStartX = Math.abs(conv.x - holdMouseX);
                        int distFromStartY = Math.abs(conv.y - holdMouseY);
                        if (distFromStartX > 5 || distFromStartY > 5) {
                            holdProgress = 0;
                            interactionTimer.stop();
                        }
                    }
                    slotsVisualPanel.repaint();
                }
            });

            slotButtons[i] = slotBtn;
            slotsVisualPanel.add(slotBtn);

            slotsVisualPanel.add(Box.createVerticalStrut(2));
        }

        fireSelectionChanged();

        slotsVisualPanel.revalidate();
        slotsVisualPanel.repaint();
    }

    private String extractId(String slotText) {
        try {
            String[] parts = slotText.split(" - ");
            if (parts.length > 1) {
                return parts[1].split(" ")[0];
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
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