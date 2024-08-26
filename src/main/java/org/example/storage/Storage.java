package org.example.storage;

import lombok.Data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@Data
public class Storage {

    private static Storage instance;
    private Connection connection;
    private String dbUrl;
    private String dbUser;
    private String dbPass;

    private Storage(String dbUrl, String dbUser, String dbPass) {
        this.dbUrl = dbUrl;
        this.dbUser = dbUser;
        this.dbPass = dbPass;
        try {
            this.connection = DriverManager.getConnection(dbUrl, dbUser, dbPass);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static Storage getInstance(String dbUrl, String dbUser, String dbPass) {
        if (instance == null) {
            instance = new Storage(dbUrl, dbUser, dbPass);
        }
        return instance;
    }

    public Connection getConnection() {
        return connection;
    }

    // Метод проверки подключения
    public boolean isConnected() {
        try {
            return connection != null && !connection.isClosed();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}

