package utils;

import java.sql.Connection;
import java.sql.DriverManager;

public class DatabaseConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/email_builder";
    private static final String USER = "root";
    private static final String PASSWORD = "Admin@123";

    public static Connection getConnection() {
        Connection connection = null;
        try {
            // Debug message to check driver load
            System.out.println("Loading MySQL Driver...");
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Debug message for connection attempt
            System.out.println("Connecting to database...");
            connection = DriverManager.getConnection(URL, USER, PASSWORD);

            // Debug message for successful connection
            System.out.println("Connection established successfully!");
        } catch (Exception e) {
            // Print error for debugging
            System.err.println("Database connection failed: " + e.getMessage());
            e.printStackTrace();
        }
        return connection;
    }
}
