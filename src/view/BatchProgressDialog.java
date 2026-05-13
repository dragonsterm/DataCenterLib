/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package view;

import javax.swing.*;
import java.awt.*;

/**
 *
 * @author mahar
 */
public class BatchProgressDialog extends JDialog {
    private JProgressBar progressBar;
    private JLabel lblStatus;

    public BatchProgressDialog() {
        setTitle("Batch Operation Progress");
        setSize(400, 150);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
        setModal(true);
        setLayout(new BorderLayout(10, 10));

        lblStatus = new JLabel("Batch Operation Starting");
        lblStatus.setFont(new Font("Arial", Font.PLAIN, 14));
        lblStatus.setBorder(BorderFactory.createEmptyBorder(15, 10, 5, 10));

        progressBar = new JProgressBar(0, 100);
        progressBar.setStringPainted(true);
        progressBar.setPreferredSize(new Dimension(360, 30));

        JPanel progressPanel = new JPanel();
        progressBar.setBorder(BorderFactory.createEmptyBorder(5, 15, 20, 15));
        progressBar.add(progressBar);

        add(lblStatus, BorderLayout.NORTH);
        add(progressPanel, BorderLayout.CENTER);
    }

    public void updateProgress(int current, int total, String msg) {
        lblStatus.setText(msg);

        if (total > 0) {
            int percentage = (int) (((double) current / total) * 100);
            progressBar.setValue(percentage);

            progressBar.setString(percentage + "% (" + current + " dari " + total + ")");
        }
    }
}
