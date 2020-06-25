package Server;

import java.sql.Connection;
import java.sql.SQLException;

import static java.sql.DriverManager.getConnection;

public class DataBase {
    /**
     * General class for DB connection! Not important witch one!
     */
    private String name; // root name for db
    private String pass; // pass for name to access db
    private String url; // db url


    private String initialUrl;

    public DataBase() {

    }

    public DataBase(String name, String pass, String url) {
        this.name = name;
        this.pass = pass;
        this.url = url + "?serverTimezone=UTC";
    }

    public DataBase(String name, String pass, String url, String init) {
        /* Constructor for Data Base. */
        this.name = name;
        this.pass = pass;
        this.url = url + "?serverTimezone=UTC"; // add serverTimezone
        this.initialUrl = init;
    }

    public Connection connectionToDB() throws SQLException {
        /* Connection function use name, pass, url */
        return getConnection(this.url, this.name, this.pass);
    }

    public void testConnection() {
        /* Test Connection if are not error write Connection successful or error*/
        try (Connection connection = connectionToDB()) {
            System.out.println("Connection to Data Base is successful!");
        } catch (SQLException e) {
            System.out.println("Connection error: \n" + e);
        }
    }

    public void setUrl(String url) {
        this.url = this.initialUrl + url + "?serverTimezone=UTC";
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public void setInitialUrl(String initialUrl) {
        this.initialUrl = initialUrl;
    }
}
