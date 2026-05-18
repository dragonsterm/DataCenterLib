package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RoomDAO {
    private Connection connection;

    public RoomDAO() {
        this.connection = DatabaseConnector.getInstance().getConnection();
    }

    public List<String> getAllRoomNames() {
        List<String> rooms = new ArrayList<>();
        String sql = "SELECT room_name FROM data_center_room";
        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                rooms.add(rs.getString("room_name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rooms;
    }

    public boolean createRoom(String roomName) {
        String sql = "INSERT INTO data_center_room (room_name) VALUES (?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, roomName);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public int getRoomIdByName(String roomName) {
        String sql = "SELECT id_room FROM data_center_room WHERE room_name = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, roomName);
            ResultSet rs = stmt.executeQuery();
            if(rs.next()) {
                return rs.getInt("id_room");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }
}
