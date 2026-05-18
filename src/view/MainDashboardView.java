/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package view;

import model.ServerRack;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 *
 * @author mahar
 */
public class MainDashboardView extends JFrame {
    private JPanel gridPanel;
    private JLabel lblTitle;
    private JMenuItem itemChangeLocation;
    private JMenuItem itemAddRoom;

    public MainDashboardView() {
        setTitle("Data Center Dashboard - Room Map");
        setSize(800, 600);
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

        gridPanel = new JPanel();
        gridPanel.setLayout(new GridLayout(5, 8, 10, 10));
        gridPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JScrollPane scrollPane = new JScrollPane(gridPanel);
        add(scrollPane, BorderLayout.CENTER);
    }

    public void setDashboardTitle(String roomName) {
        lblTitle.setText("<html>Data Center Dashboard<br>Room: " + roomName + "</html>");
        setTitle("Dashboard - " + roomName);
    }

    public JPanel getGridPanel() {
        return gridPanel;
    }

    public JMenuItem getItemChangeLocation() {
        return itemChangeLocation;
    }

    public JMenuItem getItemAddRoom() {
        return itemAddRoom;
    }
}
