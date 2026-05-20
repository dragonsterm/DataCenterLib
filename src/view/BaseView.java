/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package view;

import model.DataCenterRoom;
import javax.swing.*;
import java.awt.*;

/**
 *
 * @author mahar
 */
public class BaseView extends JFrame {
    private JMenuItem itemChangeLocation;
    private JMenuItem itemAddRoom;
    private JMenuItem itemNavDashboard;
    private JMenuItem itemNavMap;
    private JPanel cardsContainer;
    private CardLayout cardLayout;
    private DashboardView dashboardView;
    private Viewport viewport;
    private JPanel bottomToolbar;
    private JButton btnSelect;
    private JButton btnBuildRack;
    private JButton btnBuildPath;

    public BaseView() {
        setTitle("DataCoreLib");
        setSize(1280, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JMenuBar menuBar = new JMenuBar();

        JMenu menuHome = new JMenu("System");
        itemChangeLocation = new JMenuItem("Change Location");
        menuHome.add(itemChangeLocation);

        JMenu menuNav = new JMenu("Menu");
        itemNavDashboard = new JMenuItem("Dashboard");
        itemNavMap = new JMenuItem("Room Map");
        menuNav.add(itemNavDashboard);
        menuNav.add(itemNavMap);

        JMenu menuTools = new JMenu("Settings");
        itemAddRoom = new JMenuItem("Add Data Center Room");
        menuTools.add(itemAddRoom);

        menuBar.add(menuNav);
        menuBar.add(menuHome);
        menuBar.add(menuTools);
        setJMenuBar(menuBar);

        cardLayout = new CardLayout();
        cardsContainer = new JPanel(cardLayout);

        DataCenterRoom tempRoom = new DataCenterRoom("Temp");
        dashboardView = new DashboardView(tempRoom);

        JPanel mapCard = new JPanel(new BorderLayout());
        viewport = new Viewport(tempRoom);
        bottomToolbar = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        bottomToolbar.setBackground(Color.decode("#2B2D31"));
        bottomToolbar.setBorder(BorderFactory.createMatteBorder(3, 0, 0, 0, Color.decode("#F3C623")));

        btnSelect = createGameButton("Pointer (Select)");
        btnBuildRack = createGameButton("Build Rack (3x3)");
        btnBuildPath = createGameButton("Build Path (1x1)");

        bottomToolbar.add(btnSelect);
        bottomToolbar.add(btnBuildRack);
        bottomToolbar.add(btnBuildPath);

        mapCard.add(viewport, BorderLayout.CENTER);
        mapCard.add(bottomToolbar, BorderLayout.SOUTH);

        cardsContainer.add(dashboardView, "DASHBOARD");
        cardsContainer.add(mapCard, "MAP");

        add(cardsContainer, BorderLayout.CENTER);

        navToDashboard();
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
        dashboardView.updateData(room);

        JPanel mapCard = (JPanel) cardsContainer.getComponent(1);
        mapCard.remove(viewport);
        viewport = new Viewport(room);
        mapCard.add(viewport, BorderLayout.CENTER);

        revalidate();
        repaint();
    }

    public void navToDashboard() {
        cardLayout.show(cardsContainer, "DASHBOARD");
    }

    public void navToMap() {
        cardLayout.show(cardsContainer, "MAP");
    }

    public void setDashboardTitle(String roomName) {
        setTitle("DataCoreLIB - " + roomName);
    }

    public JMenuItem getItemNavDashboard() {
        return itemNavDashboard;
    }
    public JMenuItem getItemNavMap() {
        return itemNavMap;
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