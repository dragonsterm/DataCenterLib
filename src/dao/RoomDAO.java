/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import model.DataCenterRoom;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author mahar
 */
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

    public boolean createRoom(String roomName, int widthGrid, int heightGrid) {
        String sql = "INSERT INTO data_center_room (room_name, width_grid, height_grid) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, roomName);
            stmt.setInt(2, widthGrid);
            stmt.setInt(3, heightGrid);
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

    public DataCenterRoom getRoomByName(String roomName) {
        String sql = "SELECT * FROM data_center_room WHERE room_name = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, roomName);
            ResultSet rs = stmt.executeQuery();
            if(rs.next()) {
                DataCenterRoom room = new DataCenterRoom(roomName);
                room.setDimensions(rs.getInt("width_grid"), rs.getInt("height_grid"));
                return room;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new DataCenterRoom(roomName);
    }
}