/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package dao;

import model.ServerRack;
import model.GridLocation;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 *
 * @author mahar
 */
public class RackDAO {
    private Connection connection;

    public RackDAO() {
        this.connection = DatabaseConnector.getInstance().getConnection();
    }

    public boolean create(ServerRack rack, String roomName) {
        RoomDAO roomDAO = new RoomDAO();
        int roomId = roomDAO.getRoomIdByName(roomName);

        String sql = "INSERT INTO server_rack (rack_id, max_capacity_u, zone_name, x_coord, y_coord, id_room) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, rack.getRackId());
            stmt.setInt(2, rack.getMaxCapacityU());
            stmt.setString(3, rack.getLocation().getZoneName());
            stmt.setInt(4, rack.getLocation().getxCoordinate());
            stmt.setInt(5, rack.getLocation().getyCoordinate());
            stmt.setInt(6, roomId);

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public java.util.List<ServerRack> getRacksByRoom(String roomName) {
        java.util.List<ServerRack> racks = new ArrayList<>();
        String sql = "SELECT sr.* FROM server_rack sr JOIN data_center_room dcr ON sr.id_room = dcr.id_room WHERE dcr.room_name = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, roomName);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    String rackId = rs.getString("rack_id");
                    int maxCapacity = rs.getInt("max_capacity_u");
                    String zone = rs.getString("zone_name");
                    int x = rs.getInt("x_coord");
                    int y = rs.getInt("y_coord");

                    GridLocation loc = new GridLocation(x, y, zone);
                    ServerRack rack = new ServerRack(rackId, maxCapacity, loc);

                    ServerDAO serverDAO = new ServerDAO();
                    java.util.List<model.Server> servers = serverDAO.getServersByRack(rackId);
                    for(model.Server s : servers) {
                        rack.addEquipment(s, s.getStartSlot());
                    }

                    racks.add(rack);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return racks;
    }

    public boolean updateLocation(String rackId, int newX, int newY) {
        String sql = "UPDATE server_rack SET x_coord = ?, y_coord = ? WHERE rack_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, newX);
            stmt.setInt(2, newY);
            stmt.setString(3, rackId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean delete(String rackId) {
        String sqlServer = "DELETE FROM server WHERE rack_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sqlServer)) {
            stmt.setString(1, rackId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }

        String sqlRack = "DELETE FROM server_rack WHERE rack_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sqlRack)) {
            stmt.setString(1, rackId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}