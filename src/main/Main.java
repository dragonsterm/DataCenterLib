/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package main;

import controller.MainController;
import model.DataCenterRoom;
import dao.RoomDAO;
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

            RoomDAO roomDAO = new RoomDAO();
            DataCenterRoom mainRoom = roomDAO.getRoomByName("Alpha Core Room");

            dao.RackDAO rackDAO = new dao.RackDAO();
            for (ServerRack rack : rackDAO.getRacksByRoom("Alpha Core Room")) {
                mainRoom.addRack(rack);
            }

            dao.PathDAO pathDAO = new dao.PathDAO();
            for (model.RoomPath path : pathDAO.getPathsByRoom("Alpha Core Room")) {
                mainRoom.addPath(path);
            }

            MainDashboardView dashboardView = new MainDashboardView();
            MainController appController = new MainController(dashboardView, mainRoom);

            appController.initController();
        });
    }
    
}
