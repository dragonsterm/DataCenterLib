/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import model.Server;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author mahar
 */
public class ServerDAO implements ICRUD<Server> {
    private Connection connection;

    public ServerDAO() {
        this.connection = DatabaseConnector.getInstance().getConnection();
    }

    @Override
    public boolean create(Server item) {
        String query = "INSERT INTO server (id_asset, model_name, size_in_u, status, cpu_name, cpu_cores, total_ram_gb, "
                + "os_type, total_storage_gb, used_storage_gb, cpu_utilization, ram_utilization, rack_id, start_slot) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, item.getIdAsset());
            stmt.setString(2, item.getModelName());
            stmt.setInt(3, item.getSizeInU());
            stmt.setString(4, item.getStatus());
            stmt.setString(5, item.getCpuName());
            stmt.setInt(6, item.getCpuCores());
            stmt.setInt(7, item.getTotalRamGB());
            stmt.setString(8, item.getOsType());
            stmt.setInt(9, item.getTotalStorageGB());
            stmt.setInt(10, item.getUsedStorageGB());
            stmt.setDouble(11, item.getCpuUtilization());
            stmt.setDouble(12, item.getRamUtilization());
            stmt.setString(13, item.getRackId());
            stmt.setInt(14, item.getStartSlot());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Server read(String id) {
        String query = "SELECT * FROM server WHERE id_asset =?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Server server = extractServerFromResultSet(rs);
                return server;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean update(Server item) {
        String query = "UPDATE server SET model_name=?, size_in_u=?, status=?, cpu_name=?, cpu_cores=?, total_ram_gb=?, "
                + "os_type=?, total_storage_gb=?, used_storage_gb=?, cpu_utilization=?, ram_utilization=? "
                + "WHERE id_asset=?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, item.getModelName());
            stmt.setInt(2, item.getSizeInU());
            stmt.setString(3, item.getStatus());
            stmt.setString(4, item.getCpuName());
            stmt.setInt(5, item.getCpuCores());
            stmt.setInt(6, item.getTotalRamGB());
            stmt.setString(7, item.getOsType());
            stmt.setInt(8, item.getTotalStorageGB());
            stmt.setInt(9, item.getUsedStorageGB());
            stmt.setDouble(10, item.getCpuUtilization());
            stmt.setDouble(11, item.getRamUtilization());
            stmt.setString(12, item.getIdAsset());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean delete(String id) {
        String query = "DELETE FROM server WHERE id_asset = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<Server> getAll() {
        List<Server> servers = new ArrayList<>();
        String query = "SELECT * FROM server";
        try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                Server server = extractServerFromResultSet(rs);
                servers.add(server);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return servers;
    }

    public List<Server> getServersByRack(String rackId) {
        List<Server> servers = new ArrayList<>();
        String sql = "SELECT * FROM server WHERE rack_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, rackId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Server server = extractServerFromResultSet(rs);
                servers.add(server);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return servers;
    }
    public boolean moveServerRack(String serverId, String newRackId) {
        String sql = "UPDATE server SET rack_id = ? WHERE id_asset = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, newRackId);
            stmt.setString(2, serverId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public int getNextServerId() {
        int nextId = 1;
        String query = "SELECT MAX(CAST(SUBSTRING(id_asset, 5) AS UNSIGNED)) AS max_id FROM server WHERE id_asset LIKE 'SRV-%'";
        try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
            if (rs.next()) {
                nextId = rs.getInt("max_id") + 1;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return nextId;
    }

    private Server extractServerFromResultSet(ResultSet rs) throws SQLException {
        Server server = new Server(
                rs.getString("id_asset"),
                rs.getString("model_name"),
                rs.getInt("size_in_u"),
                rs.getString("status"),
                rs.getString("cpu_name"),
                rs.getInt("cpu_cores"),
                rs.getInt("total_ram_gb"),
                rs.getString("os_type"),
                rs.getInt("total_storage_gb"),
                rs.getInt("used_storage_gb")
        );
        server.setCpuUtilization(rs.getDouble("cpu_utilization"));
        server.setRamUtilization(rs.getDouble("ram_utilization"));
        server.setStartSlot(rs.getInt("start_slot"));
        return server;
    }

    public boolean updateServerSlot(String idAsset, int startSlot) {
        String query = "UPDATE server SET start_slot=? WHERE id_asset=?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, startSlot);
            stmt.setString(2, idAsset);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
