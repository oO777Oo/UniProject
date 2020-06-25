package Server;

import Client.DbData;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class Initialization {
    /**
     * Class created only for Db initialization of db
     */
    private Initialization() {
    }

    public static SqlManager sqlInitialization() throws IOException {
        /**
         * Method to init from console all needed data for connection!
         * @return sqlManager: obj type(SqlManager) with db initialized
         * */
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Write URL for DB connection!:");
        String url = reader.readLine();
        System.out.println("Write root name for db connection:");
        String root = reader.readLine();
        System.out.println("Write pass for db connection:");
        String pass = reader.readLine();
        DbData dbData = new DbData(root, pass, url, url);
        SqlManager sqlManager = new SqlManager();
        sqlManager.setMySqlParamName(dbData);
        sqlManager.getMySql().testConnection();
        return sqlManager;
    }

    public static void dataBaseCreation(SqlManager sqlManager) {
        /** In this method we create AppDev Database and call
         * dataBaseInitialization(sqlManager),
         * addDataIntoDb(sqlManager).
         * @param sqlManager: sqlManager obj
         * */
        try (Connection connection = sqlManager.getMySql().connectionToDB()) {
            Statement statement = connection.createStatement();
            statement.execute("CREATE DATABASE AppDev");
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        sqlManager.getMySql().setUrl("AppDev");
        dataBaseInitialization(sqlManager);
        addDataIntoDb(sqlManager);
    }

    private static void dataBaseInitialization(SqlManager sqlManager) {
        /** This method initializes tables for AppDev database from
         * sql file with this path "src/dataBase/dataBase.sql" - same package!
         * @param sqlManager: sqlManager obj
         * */
        BufferedReader reader;
        try (Connection connection = sqlManager.getMySql().connectionToDB()) {
            String sqlPath = "src/dataBase/dataBase.sql";
            reader = new BufferedReader(new FileReader(sqlPath));
            Statement statement = connection.createStatement();
            String line;
            while ((line = reader.readLine()) != null) {
                statement.execute(line);
            }
        } catch (SQLException | IOException exception) {
            exception.printStackTrace();
        }
    }

    private static void addDataIntoDb(SqlManager sqlManager) {
        /**
         * This method works after dataBaseInitialization!
         * Init meta data in data base
         * from sql file with path ->"src/dataBase/dataInitialization.sql" - same package!
         * And init correct connection with db for another remote class BankSystem!
         * */
        String initializationPath = "src/dataBase/dataInitialization.sql";
        try (BufferedReader reader = new BufferedReader(new FileReader(initializationPath));) {
            Connection connection = sqlManager.getMySql().connectionToDB();
            Statement statement = connection.createStatement();
            String line;
            while ((line = reader.readLine()) != null) {
                statement.execute(line);
            }
        } catch (IOException | SQLException exception) {
            exception.printStackTrace();
        }
        BankSystem.setMySqlManagement(sqlManager.getMySql());
    }
}
