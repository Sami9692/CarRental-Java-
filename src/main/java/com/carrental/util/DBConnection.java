package com.carrental.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Singleton class to manage a single database connection.
 * Uses the Singleton design pattern to ensure only one instance exists.
 */
public class DBConnection {

    private static final String URL      = "jdbc:mysql://localhost:3306/car_rental_db?useSSL=false&serverTimezone=UTC";
    private static final String USER     = "root";
    private static final String PASSWORD = "Sami@220550";

    private static DBConnection instance;
    private Connection connection;

    /** Private constructor – loads the JDBC driver and opens the connection. */
    private DBConnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            this.connection = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("[DB] Connection established successfully.");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("[DB] MySQL JDBC Driver not found. Add the JAR to your classpath.", e);
        } catch (SQLException e) {
            throw new RuntimeException("[DB] Failed to connect to the database: " + e.getMessage(), e);
        }
    }

    /**
     * Returns the single DBConnection instance (lazy initialization, not thread-safe –
     * acceptable for a console application with a single thread).
     */
    public static DBConnection getInstance() {
        if (instance == null) {
            instance = new DBConnection();
        }
        return instance;
    }

    /** Returns the raw JDBC Connection. Re-opens it if it was closed. */
    public Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                connection = DriverManager.getConnection(URL, USER, PASSWORD);
            }
        } catch (SQLException e) {
            throw new RuntimeException("[DB] Error checking/restoring connection: " + e.getMessage(), e);
        }
        return connection;
    }

    /** Closes the underlying connection. */
    public void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
                System.out.println("[DB] Connection closed.");
            } catch (SQLException e) {
                System.err.println("[DB] Error closing connection: " + e.getMessage());
            }
        }
    }
}
