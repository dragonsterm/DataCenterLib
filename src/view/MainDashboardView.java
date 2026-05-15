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

    public MainDashboardView() {
        setTitle("Data Center Dashboard - Room Map");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JLabel lblTitle = new JLabel("Data Center Dashboard - Room Map");
        lblTitle.setFont(new Font("Arial", Font.BOLD, 18));
        add(lblTitle, BorderLayout.NORTH);

        gridPanel = new JPanel();
        gridPanel.setLayout(new GridLayout(5, 8, 10, 10));
        gridPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JScrollPane scrollPane = new JScrollPane(gridPanel);
        add(scrollPane, BorderLayout.CENTER);
    }

    public void renderGrid(List<ServerRack> racks) {
        gridPanel.removeAll();
    }

    public JPanel getGridPanel() {
        return gridPanel;
    }
}
