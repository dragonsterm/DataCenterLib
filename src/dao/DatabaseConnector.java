/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import java.sql.*;

/**
 *
 * @author mahar
 */
public class DatabaseConnector {
    private static DatabaseConnector instance;
    private Connection connection;

    private final String DBNAME = "datacorelibrary";
    private final String URL_DB = "jdbc:mysql://localhost:3306/" + DBNAME;
    private final String USERNAME = "root";
    private final String PASSWORD = "";

    private DatabaseConnector() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            this.connection = DriverManager.getConnection(URL_DB, USERNAME, PASSWORD);
        } catch (ClassNotFoundException | SQLException e) {
            System.err.println("Connection Database Error: " + e.getLocalizedMessage());
        }
    }

    public static synchronized DatabaseConnector getInstance() {
        if (instance == null) {
            instance = new DatabaseConnector();
        }
        return instance;
    }

    public Connection getConnection() {
        return connection;
    }
}
