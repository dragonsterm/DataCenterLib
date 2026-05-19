package dao;

import model.RoomPath;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PathDAO {
    private Connection connection;

    public PathDAO() {
        this.connection = DatabaseConnector.getInstance().getConnection();
    }

    public boolean createPath(RoomPath path, String roomName) {
        RoomDAO roomDAO = new RoomDAO();
        int roomId = roomDAO.getRoomIdByName(roomName);

        String sql = "INSERT INTO room_path (id_room, x_coord, y_coord) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, roomId);
            stmt.setInt(2, path.getXCoord());
            stmt.setInt(3, path.getYCoord());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<RoomPath> getPathsByRoom(String roomName) {
        List<RoomPath> paths = new ArrayList<>();
        String sql = "SELECT rp.* FROM room_path rp JOIN data_center_room dcr ON rp.id_room = dcr.id_room WHERE dcr.room_name = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, roomName);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                paths.add(new RoomPath(
                        rs.getInt("id_path"),
                        rs.getInt("id_room"),
                        rs.getInt("x_coord"),
                        rs.getInt("y_coord")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return paths;
    }
}