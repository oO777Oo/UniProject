package bank.path;

import java.sql.*;

class DBConnection {
    String userName;
    String password;
    String connectionUrl;
    String driverLink;

    public DBConnection(String username, String password, String connectiontoUrl, String driverLink){
        this.userName = username;
        this.password = password;
        this.connectionUrl = connectiontoUrl;
        this.driverLink = driverLink;

    }
    public Connection getRes() throws SQLException {
        return DriverManager.getConnection(this.connectionUrl, this.userName, this.password);
    }

    public void testConnection() throws ClassNotFoundException {
        Class.forName(this.driverLink);
        try (Connection connection = DriverManager.getConnection(this.connectionUrl, this.userName, this.password);
             Statement statement = connection.createStatement()) {
            System.out.println("Connected to Data Base.");
        } catch (SQLException e){
            System.out.println("Connection error: \n" + e);
        }
    }
}

