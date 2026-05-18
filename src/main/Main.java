/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package main;

import controller.MainController;
import model.DataCenterRoom;
import model.GridLocation;
import model.ServerRack;
import view.MainDashboardView;

import javax.swing.SwingUtilities;


/**
 *
 * @author mahar
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        SwingUtilities.invokeLater(() -> {

            DataCenterRoom mainRoom = new DataCenterRoom("Alpha Core Room");
            dao.RackDAO rackDAO = new dao.RackDAO();

            for (ServerRack rack : rackDAO.getRacksByRoom("Alpha Core Room")) {
                mainRoom.addRack(rack);
            }

            MainDashboardView dashboardView = new MainDashboardView();

            MainController appController = new MainController(dashboardView, mainRoom);

            appController.initController();

        });
    }
    
}
