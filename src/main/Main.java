/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package main;

import controller.MainController;
import model.DataCenterRoom;
import dao.RoomDAO;
import model.ServerRack;
import view.BaseView;

import javax.swing.SwingUtilities;
import java.util.List;


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
            List<String> allRooms = roomDAO.getAllRoomNames();
            String roomToLoad = "Default Room";

            if (allRooms.isEmpty()) {

                boolean isCreated = roomDAO.createRoom("Default Room", 64, 64);
                if (isCreated) {
                    roomToLoad = "Default Room";
                }
            } else if (!allRooms.contains(roomToLoad)) {
                roomToLoad = allRooms.get(0);
            }
            DataCenterRoom mainRoom = roomDAO.getRoomByName(roomToLoad);

            dao.RackDAO rackDAO = new dao.RackDAO();
            for (ServerRack rack : rackDAO.getRacksByRoom(roomToLoad)) {
                mainRoom.addRack(rack);
            }

            dao.PathDAO pathDAO = new dao.PathDAO();
            for (model.RoomPath path : pathDAO.getPathsByRoom(roomToLoad)) {
                mainRoom.addPath(path);
            }

            BaseView dashboardView = new BaseView();
            MainController appController = new MainController(dashboardView, mainRoom);

            appController.initController();
        });
    }

}
