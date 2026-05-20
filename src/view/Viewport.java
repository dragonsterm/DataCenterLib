/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package view;

import controller.RackController;
import dao.RackDAO;
import dao.ServerDAO;
import dao.PathDAO;
import model.DataCenterRoom;
import model.GridLocation;
import model.RoomPath;
import model.ServerRack;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Arc2D;
import java.awt.geom.Ellipse2D;

/**
 *
 * @author mahar
 */
public class Viewport extends JPanel {
    private DataCenterRoom room;
    private int cameraX = 0;
    private int cameraY = 0;
    private final int TILE_SIZE = 40;

    private int lastMouseX, lastMouseY;
    private int holdMouseX, holdMouseY;
    private boolean isClicking = false;

    private boolean upPressed, downPressed, leftPressed, rightPressed;
    private Timer cameraTimer;

    public enum Mode { SELECT, BUILD_RACK, BUILD_PATH, DEMOLISH }
    private Mode currentMode = Mode.SELECT;

    private Timer interactionTimer;
    private int holdProgress = 0;
    private boolean isHoldingRack = false;
    private ServerRack selectedRackToMove = null;

    public Viewport(DataCenterRoom room) {
        this.room = room;
        setBackground(Color.decode("#1E1E1E"));
        setFocusable(true);
        requestFocusInWindow();

        setupMouseInteractions();
        setupKeyboardInteractions();

        cameraTimer = new Timer(16, e -> updateCamera());
        cameraTimer.start();

        interactionTimer = new Timer(10, e -> {
            if (isClicking && currentMode == Mode.SELECT && !isHoldingRack) {
                Point gridPos = screenToGrid(holdMouseX, holdMouseY);
                ServerRack rack = getRackAt(gridPos.x, gridPos.y);
                if (rack != null) {
                    holdProgress++;
                    if (holdProgress >= 100) {
                        isHoldingRack = true;
                        selectedRackToMove = rack;
                        holdProgress = 0;
                    }
                    repaint();
                } else {
                    holdProgress = 0;
                }
            }
        });
        interactionTimer.setInitialDelay(250);
    }

    public void setMode(Mode mode) {
        this.currentMode = mode;
        this.isHoldingRack = false;
        this.selectedRackToMove = null;
        repaint();
    }

    private void setupKeyboardInteractions() {
        InputMap im = getInputMap(WHEN_IN_FOCUSED_WINDOW);
        ActionMap am = getActionMap();
        im.put(KeyStroke.getKeyStroke("pressed W"), "up_pressed");
        im.put(KeyStroke.getKeyStroke("released W"), "up_released");
        im.put(KeyStroke.getKeyStroke("pressed A"), "left_pressed");
        im.put(KeyStroke.getKeyStroke("released A"), "left_released");
        im.put(KeyStroke.getKeyStroke("pressed S"), "down_pressed");
        im.put(KeyStroke.getKeyStroke("released S"), "down_released");
        im.put(KeyStroke.getKeyStroke("pressed D"), "right_pressed");
        im.put(KeyStroke.getKeyStroke("released D"), "right_released");

        am.put("up_pressed", new AbstractAction() { public void actionPerformed(ActionEvent e) { upPressed = true; } });
        am.put("up_released", new AbstractAction() { public void actionPerformed(ActionEvent e) { upPressed = false; } });
        am.put("left_pressed", new AbstractAction() { public void actionPerformed(ActionEvent e) { leftPressed = true; } });
        am.put("left_released", new AbstractAction() { public void actionPerformed(ActionEvent e) { leftPressed = false; } });
        am.put("down_pressed", new AbstractAction() { public void actionPerformed(ActionEvent e) { downPressed = true; } });
        am.put("down_released", new AbstractAction() { public void actionPerformed(ActionEvent e) { downPressed = false; } });
        am.put("right_pressed", new AbstractAction() { public void actionPerformed(ActionEvent e) { rightPressed = true; } });
        am.put("right_released", new AbstractAction() { public void actionPerformed(ActionEvent e) { rightPressed = false; } });
    }

    private void updateCamera() {
        int speed = 12;
        boolean moved = false;
        if (upPressed) { cameraY -= speed; moved = true; }
        if (downPressed) { cameraY += speed; moved = true; }
        if (leftPressed) { cameraX -= speed; moved = true; }
        if (rightPressed) { cameraX += speed; moved = true; }
        if (moved) repaint();
    }

    private void setupMouseInteractions() {
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                lastMouseX = e.getX();
                lastMouseY = e.getY();
                holdMouseX = e.getX();
                holdMouseY = e.getY();
                isClicking = true;

                if (SwingUtilities.isLeftMouseButton(e) && currentMode == Mode.SELECT) {
                    holdProgress = 0;
                    interactionTimer.restart();
                }
                repaint();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                isClicking = false;
                interactionTimer.stop();

                if (SwingUtilities.isLeftMouseButton(e)) {
                    Point gridPos = screenToGrid(e.getX(), e.getY());

                    if (currentMode == Mode.DEMOLISH) {
                        ServerRack clickedRack = getRackAt(gridPos.x, gridPos.y);
                        if (clickedRack != null) {
                            int confirm = JOptionPane.showConfirmDialog(Viewport.this,
                                    "Do You want to Demolish this Rack Server '" + clickedRack.getRackId() + "'?\nEvery Server in this rack will get deleted permanently",
                                    "Confirm Demolish", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

                            if (confirm == JOptionPane.YES_OPTION) {
                                RackDAO rackDAO = new RackDAO();
                                if (rackDAO.delete(clickedRack.getRackId())) {
                                    room.getAllRacks().remove(clickedRack);
                                } else {
                                    JOptionPane.showMessageDialog(Viewport.this, "Gagal menghapus Rak dari database.");
                                }
                            }
                        } else {
                            RoomPath clickedPath = getPathAt(gridPos.x, gridPos.y);
                            if (clickedPath != null) {
                                PathDAO pathDAO = new PathDAO();
                                if (pathDAO.deletePath(gridPos.x, gridPos.y, room.getRoomName())) {
                                    room.getPaths().remove(clickedPath);
                                }
                            }
                        }
                    } else if (!isHoldingRack && holdProgress < 100) {
                        if (currentMode == Mode.BUILD_PATH) {
                            buildPath(gridPos.x, gridPos.y);
                        } else if (currentMode == Mode.BUILD_RACK) {
                            buildRack(gridPos.x, gridPos.y);
                        } else if (currentMode == Mode.SELECT) {
                            ServerRack clickedRack = getRackAt(gridPos.x, gridPos.y);
                            if (clickedRack != null) {
                                ServerDAO serverDAO = new ServerDAO();
                                RackController rackController = new RackController(room, clickedRack, serverDAO);
                                rackController.loadVisualRack(clickedRack.getRackId());
                            }
                        }
                    } else if (isHoldingRack && selectedRackToMove != null) {
                        isHoldingRack = false;
                        if (canPlaceRack(gridPos.x, gridPos.y, selectedRackToMove)) {
                            RackDAO rackDAO = new RackDAO();
                            boolean isSaved = rackDAO.updateLocation(selectedRackToMove.getRackId(), gridPos.x, gridPos.y);
                            if(isSaved) {
                                selectedRackToMove.getLocation().setCoordinates(gridPos.x, gridPos.y);
                            }
                        }
                        selectedRackToMove = null;
                    }
                }
                holdProgress = 0;
                repaint();
            }
        });

        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                lastMouseX = e.getX();
                lastMouseY = e.getY();
                if (interactionTimer.isRunning() && !isHoldingRack) {
                    int distFromStartX = e.getX() - holdMouseX;
                    int distFromStartY = e.getY() - holdMouseY;
                    if (Math.abs(distFromStartX) > 5 || Math.abs(distFromStartY) > 5) {
                        holdProgress = 0;
                        interactionTimer.stop();
                    }
                }
                repaint();
            }
            @Override
            public void mouseMoved(MouseEvent e) {
                lastMouseX = e.getX();
                lastMouseY = e.getY();
                repaint();
            }
        });
    }

    private ServerRack getRackAt(int gridX, int gridY) {
        for (ServerRack rack : room.getAllRacks()) {
            int rx = rack.getLocation().getxCoordinate();
            int ry = rack.getLocation().getyCoordinate();
            if (gridX >= rx && gridX < rx + 3 && gridY >= ry && gridY < ry + 3) {
                return rack;
            }
        }
        return null;
    }

    private RoomPath getPathAt(int gridX, int gridY) {
        for (RoomPath p : room.getPaths()) {
            if (p.getXCoord() == gridX && p.getYCoord() == gridY) return p;
        }
        return null;
    }

    private boolean isPathAt(int gridX, int gridY) {
        return getPathAt(gridX, gridY) != null;
    }

    private boolean canPlaceRack(int startX, int startY, ServerRack ignoreRack) {
        if (startX < 0 || startY < 0 || startX + 3 > room.getWidthGrid() || startY + 3 > room.getHeightGrid()) {
            return false;
        }
        for (int x = startX; x < startX + 3; x++) {
            for (int y = startY; y < startY + 3; y++) {
                if (isPathAt(x, y)) return false;
                ServerRack r = getRackAt(x, y);
                if (r != null && r != ignoreRack) return false;
            }
        }
        return true;
    }

    private void buildPath(int x, int y) {
        if (x >= 0 && y >= 0 && x < room.getWidthGrid() && y < room.getHeightGrid() && getRackAt(x, y) == null && !isPathAt(x, y)) {
            PathDAO pathDAO = new PathDAO();
            RoomPath newPath = new RoomPath(0, 0, x, y);
            if (pathDAO.createPath(newPath, room.getRoomName())) {
                room.addPath(newPath);
                repaint();
            }
        }
    }

    private void buildRack(int x, int y) {
        if (canPlaceRack(x, y, null)) {
            JTextField txtRackId = new JTextField();
            JTextField txtMaxCapacity = new JTextField("42");
            JTextField txtZone = new JTextField("ZONA A");

            Object[] message = {
                    "Masukkan ID Rak (Misal: RACK-A1):", txtRackId,
                    "Kapasitas Slot (U):", txtMaxCapacity,
                    "Nama Zona Letak:", txtZone
            };

            int option = JOptionPane.showConfirmDialog(this, message, "Tambah Server Rack Baru (3x3)", JOptionPane.OK_CANCEL_OPTION);

            if (option == JOptionPane.OK_OPTION) {
                try {
                    String id = txtRackId.getText();
                    int capacity = Integer.parseInt(txtMaxCapacity.getText());
                    String zone = txtZone.getText();

                    if (id == null || id.trim().isEmpty()) return;

                    GridLocation loc = new GridLocation(x, y, zone);
                    ServerRack newRack = new ServerRack(id, capacity, loc);

                    RackDAO rackDAO = new RackDAO();
                    if (rackDAO.create(newRack, room.getRoomName())) {
                        room.addRack(newRack);
                        repaint();
                    } else {
                        JOptionPane.showMessageDialog(this, "Gagal menyimpan (ID mungkin duplikat).");
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "Kapasitas harus berupa angka!");
                }
            }
        }
    }

    public Point screenToGrid(int screenX, int screenY) {
        return new Point((screenX + cameraX) / TILE_SIZE, (screenY + cameraY) / TILE_SIZE);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2d.translate(-cameraX, -cameraY);

        int rw = room.getWidthGrid();
        int rh = room.getHeightGrid();

        g2d.setColor(Color.decode("#2C3E50"));
        g2d.fillRect(0, 0, rw * TILE_SIZE, rh * TILE_SIZE);
        g2d.setColor(Color.decode("#F1C40F"));
        g2d.setStroke(new BasicStroke(3));
        g2d.drawRect(0, 0, rw * TILE_SIZE, rh * TILE_SIZE);

        g2d.setColor(new Color(255, 255, 255, 15));
        g2d.setStroke(new BasicStroke(1));
        for (int x = 0; x <= rw; x++) g2d.drawLine(x * TILE_SIZE, 0, x * TILE_SIZE, rh * TILE_SIZE);
        for (int y = 0; y <= rh; y++) g2d.drawLine(0, y * TILE_SIZE, rw * TILE_SIZE, y * TILE_SIZE);

        for (RoomPath p : room.getPaths()) {
            g2d.setColor(Color.decode("#7F8C8D"));
            g2d.fillRect(p.getXCoord() * TILE_SIZE, p.getYCoord() * TILE_SIZE, TILE_SIZE, TILE_SIZE);
            g2d.setColor(Color.decode("#95A5A6"));
            g2d.drawRect(p.getXCoord() * TILE_SIZE, p.getYCoord() * TILE_SIZE, TILE_SIZE, TILE_SIZE);
        }

        for (ServerRack r : room.getAllRacks()) {
            if (r == selectedRackToMove) continue;

            int rx = r.getLocation().getxCoordinate() * TILE_SIZE;
            int ry = r.getLocation().getyCoordinate() * TILE_SIZE;
            int rSize = TILE_SIZE * 3;

            g2d.setColor(Color.decode("#1A252F"));
            g2d.fillRect(rx, ry, rSize, rSize);
            g2d.setColor(Color.decode("#34495E"));
            g2d.fillRect(rx + 5, ry + 25, rSize - 10, rSize - 50);

            g2d.setColor(Color.decode("#3498DB"));
            g2d.setStroke(new BasicStroke(2));
            g2d.drawRect(rx, ry, rSize, rSize);

            g2d.setColor(Color.WHITE);
            g2d.setFont(new Font("SansSerif", Font.BOLD, 12));
            g2d.drawString("Rack: " + r.getRackId(), rx + 8, ry + 18);

            int maxU = r.getMaxCapacityU();
            int usedU = maxU - r.getAvailableU();

            g2d.setFont(new Font("Monospaced", Font.BOLD, 11));
            g2d.setColor(Color.decode("#F1C40F"));
            g2d.drawString("Cap: " + usedU + "/" + maxU + " U", rx + 8, ry + rSize - 10);
        }

        Point hoverGrid = screenToGrid(lastMouseX, lastMouseY);
        if (currentMode == Mode.DEMOLISH) {
            ServerRack hoverRack = getRackAt(hoverGrid.x, hoverGrid.y);
            if (hoverRack != null) {
                g2d.setColor(new Color(231, 76, 60, 150));
                g2d.fillRect(hoverRack.getLocation().getxCoordinate() * TILE_SIZE,
                        hoverRack.getLocation().getyCoordinate() * TILE_SIZE,
                        TILE_SIZE * 3, TILE_SIZE * 3);
            } else if (isPathAt(hoverGrid.x, hoverGrid.y)) {
                g2d.setColor(new Color(231, 76, 60, 150));
                g2d.fillRect(hoverGrid.x * TILE_SIZE, hoverGrid.y * TILE_SIZE, TILE_SIZE, TILE_SIZE);
            } else {
                g2d.setColor(new Color(231, 76, 60, 80));
                g2d.fillRect(hoverGrid.x * TILE_SIZE, hoverGrid.y * TILE_SIZE, TILE_SIZE, TILE_SIZE);
            }
        } else if (currentMode == Mode.BUILD_RACK || isHoldingRack) {
            boolean valid = canPlaceRack(hoverGrid.x, hoverGrid.y, selectedRackToMove);
            g2d.setColor(valid ? new Color(46, 204, 113, 150) : new Color(231, 76, 60, 150));
            g2d.fillRect(hoverGrid.x * TILE_SIZE, hoverGrid.y * TILE_SIZE, TILE_SIZE * 3, TILE_SIZE * 3);
        } else if (currentMode == Mode.BUILD_PATH) {
            boolean validPath = getRackAt(hoverGrid.x, hoverGrid.y) == null && !isPathAt(hoverGrid.x, hoverGrid.y);
            g2d.setColor(validPath ? new Color(241, 196, 15, 150) : new Color(231, 76, 60, 150));
            g2d.fillRect(hoverGrid.x * TILE_SIZE, hoverGrid.y * TILE_SIZE, TILE_SIZE, TILE_SIZE);
        }

        g2d.translate(cameraX, cameraY);

        if (isClicking && currentMode == Mode.SELECT && !isHoldingRack && holdProgress > 0) {
            double radius = 18.0;
            double cx = holdMouseX - radius;
            double cy = holdMouseY - radius;

            g2d.setColor(new Color(0, 0, 0, 120));
            g2d.setStroke(new BasicStroke(5, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            g2d.draw(new Ellipse2D.Double(cx, cy, radius * 2, radius * 2));

            g2d.setColor(Color.WHITE);
            double angle = 360.0 * (holdProgress / 100.0);
            g2d.draw(new Arc2D.Double(cx, cy, radius * 2, radius * 2, 90, -angle, Arc2D.OPEN));
        }
    }
}