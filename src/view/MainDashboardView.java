/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package view;

import model.DataCenterRoom;
import javax.swing.*;
import java.awt.*;

/**
 *
 * @author mahar
 */
public class MainDashboardView extends JFrame {
    private JLabel lblTitle;
    private JMenuItem itemChangeLocation;
    private JMenuItem itemAddRoom;

    private Viewport viewport;
    private JButton btnSelect;
    private JButton btnBuildRack;
    private JButton btnBuildPath;

    public MainDashboardView() {
        setTitle("Data Center Dashboard - Factory Mode");
        setSize(1024, 768);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JMenuBar menuBar = new JMenuBar();
        JMenu menuHome = new JMenu("Home");
        itemChangeLocation = new JMenuItem("Change Location");
        menuHome.add(itemChangeLocation);

        JMenu menuTools = new JMenu("Tools");
        itemAddRoom = new JMenuItem("Add Room");
        menuTools.add(itemAddRoom);

        menuBar.add(menuHome);
        menuBar.add(menuTools);
        setJMenuBar(menuBar);

        lblTitle = new JLabel("<html>Data Center Dashboard<br>Room: Alpha Core Room</html>");
        lblTitle.setFont(new Font("Arial", Font.BOLD, 18));
        lblTitle.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(lblTitle, BorderLayout.NORTH);

        viewport = new Viewport(new DataCenterRoom("Temp"));
        add(viewport, BorderLayout.CENTER);

        JPanel bottomToolbar = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        bottomToolbar.setBackground(Color.decode("#2B2D31"));
        bottomToolbar.setBorder(BorderFactory.createMatteBorder(3, 0, 0, 0, Color.decode("#F3C623")));

        btnSelect = createGameButton("Pointer (Select)");
        btnBuildRack = createGameButton("Build Rack (3x3)");
        btnBuildPath = createGameButton("Build Path (1x1)");

        bottomToolbar.add(btnSelect);
        bottomToolbar.add(btnBuildRack);
        bottomToolbar.add(btnBuildPath);

        add(bottomToolbar, BorderLayout.SOUTH);
    }

    private JButton createGameButton(String text) {
        JButton btn = new JButton(text);
        btn.setBackground(Color.decode("#4F545C"));
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setFont(new Font("Arial", Font.BOLD, 14));
        btn.setPreferredSize(new Dimension(200, 40));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    public void updateViewportRoom(DataCenterRoom room) {
        remove(viewport);
        viewport = new Viewport(room);
        add(viewport, BorderLayout.CENTER);
        revalidate();
        repaint();
    }

    public void setDashboardTitle(String roomName) {
        lblTitle.setText("<html>Data Center Dashboard<br>Room: " + roomName + "</html>");
        setTitle("Dashboard - " + roomName);
    }

    public Viewport getViewport() {
        return viewport;
    }
    public JButton getBtnSelect() {
        return btnSelect;
    }
    public JButton getBtnBuildRack() {
        return btnBuildRack;
    }
    public JButton getBtnBuildPath() {
        return btnBuildPath;
    }
    public JMenuItem getItemChangeLocation() {
        return itemChangeLocation;
    }
    public JMenuItem getItemAddRoom() {
        return itemAddRoom;
    }
}