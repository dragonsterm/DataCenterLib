/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package view;

import model.DataCenterRoom;
import model.HardwareEquipment;
import model.Server;
import model.ServerRack;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.util.List;

/**
 *
 * @author mahar
 */
public class DashboardView extends JPanel {
    private DataCenterRoom room;
    private int totalRacks = 0;
    private int totalCapacityU = 0;
    private int usedCapacityU = 0;
    private int totalServers = 0;
    private int onlineServers = 0;
    private int offlineServers = 0;
    private int maintServers = 0;
    private int totalCores = 0;
    private int totalRamGB = 0;
    private int totalStorageGB = 0;
    private double avgCpu = 0;
    private double avgRam = 0;

    public DashboardView(DataCenterRoom room) {
        this.room = room;
        setLayout(new BorderLayout());
        setBackground(Color.decode("#F3F4F6"));

        calculateMetrics();
        buildUI();
    }

    public void updateData(DataCenterRoom room) {
        this.room = room;
        calculateMetrics();
        removeAll();
        buildUI();
        revalidate();
        repaint();
    }

    private void calculateMetrics() {
        totalRacks = 0; totalCapacityU = 0; usedCapacityU = 0;
        totalServers = 0; onlineServers = 0; offlineServers = 0; maintServers = 0;
        totalCores = 0; totalRamGB = 0; totalStorageGB = 0;

        double sumCpu = 0; double sumRam = 0;
        int activeComputeNodes = 0;

        if (room != null && room.getAllRacks() != null) {
            totalRacks = room.getAllRacks().size();
            for (ServerRack rack : room.getAllRacks()) {
                totalCapacityU += rack.getMaxCapacityU();
                usedCapacityU += (rack.getMaxCapacityU() - rack.getAvailableU());

                List<HardwareEquipment> equipments = rack.getHardwareList();
                for (HardwareEquipment hw : equipments) {
                    if (hw instanceof Server) {
                        Server s = (Server) hw;
                        totalServers++;
                        totalCores += s.getCpuCores();
                        totalRamGB += s.getTotalRamGB();
                        totalStorageGB += s.getTotalStorageGB();

                        String status = s.getStatus();
                        if ("Online".equalsIgnoreCase(status)) {
                            onlineServers++;
                            sumCpu += s.getCpuUtilization();
                            sumRam += s.getRamUtilization();
                            activeComputeNodes++;
                        } else if ("Offline".equalsIgnoreCase(status)) {
                            offlineServers++;
                        } else {
                            maintServers++;
                        }
                    }
                }
            }
        }

        avgCpu = activeComputeNodes > 0 ? (sumCpu / activeComputeNodes) : 0;
        avgRam = activeComputeNodes > 0 ? (sumRam / activeComputeNodes) : 0;
    }

    private void buildUI() {
        JPanel wrapper = new JPanel(new BorderLayout(0, 25));
        wrapper.setBackground(Color.decode("#F3F4F6"));
        wrapper.setBorder(new EmptyBorder(25, 30, 25, 30));

        JPanel topContainer = new JPanel(new BorderLayout(0, 20));
        topContainer.setBackground(Color.decode("#F3F4F6"));

        JLabel lblHeader = new JLabel("Dashboard - " + (room != null ? room.getRoomName() : "Unknown"));
        lblHeader.setFont(new Font("SansSerif", Font.BOLD, 26));
        lblHeader.setForeground(Color.decode("#1F2937"));
        topContainer.add(lblHeader, BorderLayout.NORTH);

        JPanel cardsPanel = new JPanel(new GridLayout(1, 3, 25, 0));
        cardsPanel.setBackground(Color.decode("#F3F4F6"));
        cardsPanel.setPreferredSize(new Dimension(0, 130));

        String availHtml = String.format("<html><span style='font-size:26px; color:#10B981'><b>%d</b></span> Online<br><span style='font-size:12px; color:#6B7280'>%d Offline | %d Maint</span></html>", onlineServers, offlineServers, maintServers);
        cardsPanel.add(createSummaryCard("Server Availability", availHtml, "Total: " + totalServers + " Devices"));

        int spacePct = totalCapacityU > 0 ? (usedCapacityU * 100 / totalCapacityU) : 0;
        String spaceHtml = String.format("<html><span style='font-size:26px; color:#3B82F6'><b>%d%%</b></span> Used<br><span style='font-size:12px; color:#6B7280'>%d U / %d U</span></html>", spacePct, usedCapacityU, totalCapacityU);
        cardsPanel.add(createSummaryCard("Server Allocation", spaceHtml, totalRacks + " Racks Allocated"));

        String computeHtml = String.format("<html><span style='font-size:26px; color:#8B5CF6'><b>%d</b></span> Cores<br><span style='font-size:12px; color:#6B7280'>%d GB RAM | %d GB Disk</span></html>", totalCores, totalRamGB, totalStorageGB);
        cardsPanel.add(createSummaryCard("Total Compute Capacity", computeHtml, "Hardware Capacity"));

        topContainer.add(cardsPanel, BorderLayout.CENTER);
        wrapper.add(topContainer, BorderLayout.NORTH);

        JPanel chartsPanel = new JPanel(new GridLayout(1, 2, 25, 0));
        chartsPanel.setBackground(Color.decode("#F3F4F6"));

        JPanel pnlDonut = createChartContainer("Servers Availability Distribution");
        pnlDonut.add(new DonutChartPanel(), BorderLayout.CENTER);

        JPanel donutLegend = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        donutLegend.setBackground(Color.WHITE);
        donutLegend.add(createLegendItem("Online", Color.decode("#10B981")));
        donutLegend.add(createLegendItem("Offline", Color.decode("#EF4444")));
        donutLegend.add(createLegendItem("Maintenance", Color.decode("#F59E0B")));
        pnlDonut.add(donutLegend, BorderLayout.SOUTH);

        JPanel pnlBar = createChartContainer("Average Resource Utilization (%)");
        pnlBar.add(new ModernBarChartPanel(), BorderLayout.CENTER);

        chartsPanel.add(pnlDonut);
        chartsPanel.add(pnlBar);

        wrapper.add(chartsPanel, BorderLayout.CENTER);

        if (offlineServers > 0) {
            JPanel pnlAlert = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 12));
            pnlAlert.setBackground(Color.decode("#FEE2E2"));
            pnlAlert.setBorder(new LineBorder(Color.decode("#F87171"), 1, true));
            
            JButton btnAlert = new JButton("CRITICAL ALARM: " + offlineServers + " Server(s) detected as Offline. Click here for details.");
            btnAlert.setFont(new Font("SansSerif", Font.BOLD, 14));
            btnAlert.setForeground(Color.decode("#B91C1C"));
            btnAlert.setContentAreaFilled(false);
            btnAlert.setBorderPainted(false);
            btnAlert.setFocusPainted(false);
            btnAlert.setCursor(new Cursor(Cursor.HAND_CURSOR));
            
            btnAlert.addActionListener(e -> {
                JOptionPane.showMessageDialog(this,
                    "Terdapat " + offlineServers + " Server offline di Ruangan: " + (room != null ? room.getRoomName() : "Unknown") + ".\n" +
                    "Harap segera periksa status daya atau jaringan di rak yang terkait.",
                    "Critical Alarm Details", 
                    JOptionPane.WARNING_MESSAGE);
            });

            pnlAlert.add(btnAlert);
            wrapper.add(pnlAlert, BorderLayout.SOUTH);
        }

        add(wrapper, BorderLayout.CENTER);
    }

    private JPanel createSummaryCard(String title, String mainValueHtml, String footer) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(Color.decode("#E5E7EB"), 1, true),
                new EmptyBorder(15, 20, 15, 20)
        ));

        JLabel lblTitle = new JLabel(title);
        lblTitle.setFont(new Font("SansSerif", Font.BOLD, 13));
        lblTitle.setForeground(Color.decode("#6B7280"));
        card.add(lblTitle, BorderLayout.NORTH);

        JLabel lblValue = new JLabel(mainValueHtml);
        card.add(lblValue, BorderLayout.CENTER);

        JLabel lblFooter = new JLabel(footer);
        lblFooter.setFont(new Font("SansSerif", Font.PLAIN, 11));
        lblFooter.setForeground(Color.decode("#9CA3AF"));
        card.add(lblFooter, BorderLayout.SOUTH);

        return card;
    }

    private JPanel createChartContainer(String title) {
        JPanel container = new JPanel(new BorderLayout());
        container.setBackground(Color.WHITE);
        container.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(Color.decode("#E5E7EB"), 1, true),
                new EmptyBorder(15, 15, 15, 15)
        ));

        JLabel lblTitle = new JLabel(title);
        lblTitle.setFont(new Font("SansSerif", Font.BOLD, 15));
        lblTitle.setForeground(Color.decode("#374151"));
        lblTitle.setBorder(new EmptyBorder(0, 0, 15, 0));
        container.add(lblTitle, BorderLayout.NORTH);

        return container;
    }

    private JPanel createLegendItem(String label, Color color) {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        p.setBackground(Color.WHITE);
        JPanel colorBox = new JPanel();
        colorBox.setBackground(color);
        colorBox.setPreferredSize(new Dimension(14, 14));
        p.add(colorBox);
        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("SansSerif", Font.PLAIN, 12));
        lbl.setForeground(Color.decode("#4B5563"));
        p.add(lbl);
        return p;
    }

    private class DonutChartPanel extends JPanel {
        public DonutChartPanel() { setBackground(Color.WHITE); }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            if (totalServers == 0) {
                g2d.setColor(Color.LIGHT_GRAY);
                g2d.drawString("No Data Available", getWidth()/2 - 50, getHeight()/2);
                return;
            }

            int w = getWidth();
            int h = getHeight();
            int diameter = Math.min(w, h) - 40;
            int x = (w - diameter) / 2;
            int y = (h - diameter) / 2;

            int angleOnline = (int) Math.round((double) onlineServers / totalServers * 360);
            int angleOffline = (int) Math.round((double) offlineServers / totalServers * 360);
            int angleMaint = 360 - angleOnline - angleOffline;

            g2d.setColor(Color.decode("#10B981"));
            g2d.fillArc(x, y, diameter, diameter, 90, -angleOnline);

            g2d.setColor(Color.decode("#EF4444"));
            g2d.fillArc(x, y, diameter, diameter, 90 - angleOnline, -angleOffline);

            g2d.setColor(Color.decode("#F59E0B"));
            g2d.fillArc(x, y, diameter, diameter, 90 - angleOnline - angleOffline, -angleMaint);

            int holeDiameter = (int)(diameter * 0.65);
            int holeX = x + (diameter - holeDiameter) / 2;
            int holeY = y + (diameter - holeDiameter) / 2;

            g2d.setColor(Color.WHITE);
            g2d.fillOval(holeX, holeY, holeDiameter, holeDiameter);

            String centerText1 = String.valueOf(totalServers);
            String centerText2 = "Total Devices";

            g2d.setColor(Color.decode("#1F2937"));
            g2d.setFont(new Font("SansSerif", Font.BOLD, 28));
            FontMetrics fm1 = g2d.getFontMetrics();
            g2d.drawString(centerText1, w/2 - fm1.stringWidth(centerText1)/2, h/2 + 5);

            g2d.setColor(Color.decode("#6B7280"));
            g2d.setFont(new Font("SansSerif", Font.PLAIN, 12));
            FontMetrics fm2 = g2d.getFontMetrics();
            g2d.drawString(centerText2, w/2 - fm2.stringWidth(centerText2)/2, h/2 + 25);
        }
    }

    private class ModernBarChartPanel extends JPanel {
        public ModernBarChartPanel() { setBackground(Color.WHITE); }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int w = getWidth();
            int h = getHeight();
            int padding = 40;
            int maxBarHeight = h - (padding * 2);

            g2d.setColor(Color.decode("#E5E7EB"));
            g2d.setStroke(new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]{5}, 0));
            for (int i = 0; i <= 5; i++) {
                int yLine = padding + (maxBarHeight * i / 5);
                g2d.drawLine(padding, yLine, w - padding, yLine);
                g2d.setColor(Color.decode("#9CA3AF"));
                g2d.setFont(new Font("SansSerif", Font.PLAIN, 10));
                String yLabel = String.valueOf(100 - (i * 20));
                g2d.drawString(yLabel, padding - 25, yLine + 4);
                g2d.setColor(Color.decode("#E5E7EB"));
            }

            int barWidth = 60;
            int spaceBetween = (w - (padding * 2) - (barWidth * 2)) / 3;

            int cpuH = (int) ((avgCpu / 100.0) * maxBarHeight);
            int cpuX = padding + spaceBetween;
            int cpuY = h - padding - cpuH;
            g2d.setColor(Color.decode("#8B5CF6"));
            g2d.fillRect(cpuX, cpuY, barWidth, cpuH);

            g2d.setColor(Color.decode("#374151"));
            g2d.setFont(new Font("SansSerif", Font.BOLD, 12));
            g2d.drawString("CPU", cpuX + 15, h - padding + 20);
            g2d.drawString(String.format("%.1f%%", avgCpu), cpuX + 10, cpuY - 10);

            int ramH = (int) ((avgRam / 100.0) * maxBarHeight);
            int ramX = cpuX + barWidth + spaceBetween;
            int ramY = h - padding - ramH;
            g2d.setColor(Color.decode("#3B82F6"));
            g2d.fillRect(ramX, ramY, barWidth, ramH);

            g2d.setColor(Color.decode("#374151"));
            g2d.drawString("RAM", ramX + 15, h - padding + 20);
            g2d.drawString(String.format("%.1f%%", avgRam), ramX + 10, ramY - 10);

            g2d.setColor(Color.decode("#9CA3AF"));
            g2d.setStroke(new BasicStroke(2));
            g2d.drawLine(padding, h - padding, w - padding + 10, h - padding);
        }
    }
}