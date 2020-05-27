package bank.path;

import java.io.*;
import java.sql.*;
import java.util.ArrayList;


public class Bank implements CreditDecision{

    private static final Bank bank = new Bank();
    // private static int costumerNr = 0;
    // private static int workerCounter = 0;
    private static final String root = "root";
    private static final String rootPass = "123qwert";
    private static final String connectUrl = "jdbc:mysql://localhost:3306/";
    private static final String timeZone = "?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";
    private static final String driverLink = "com.mysql.cj.jdbc.Driver";
    static final DBConnection db = new DBConnection(root, rootPass,connectUrl + timeZone, driverLink);
    // static String bankCode = "BLZBLZ";
    static Worker supervisor;
    static ArrayList<ArrayList<Object>> creditWaitingList = new ArrayList<>();
    private static boolean initializationCounter = true;
    
    private Bank() { }

    /** SQL Script - DB initialization */
    public static void dbInitialization() throws IOException, SQLException {
        if (initializationCounter) {
            String filepath = "src/bank/path/AppDev2_DB_creation.sql";
            Connection connection = null;
            BufferedReader reader = null;
            Statement statement;
            try {
                connection = db.getRes();
                statement = connection.createStatement();
                reader = new BufferedReader(new FileReader(filepath));
                String line;
                while ((line = reader.readLine()) != null) {
                    statement.execute(line);
                }
            } catch (SQLException e) {
                System.out.println("Can't create database 'AppDev'; database exists");
            } finally {
                if (reader != null) {
                    reader.close();
                }
                if (connection != null) {
                    connection.close();
                }
                initializationCounter = false;

            }
        } else {
            System.out.println("DB is already exist");
        }
    }

    public static creditRates creditRate(Credit credit, Connection connection) throws SQLException {
        creditRates rate;
        String getAccount = "SELECT creditRating FROM Account WHERE costumerNr = " + credit.getCostumerNr() + " ;";
        double creditRating = 0;

        ResultSet data = connection.createStatement().executeQuery(getAccount);

        while (data.next()) {
            creditRating = data.getDouble("creditRating");
        }

        if (creditRating <= 5) {
            rate = creditRates.Bad;
        } else if (creditRating > 5 && creditRating <= 6.5) {
            rate = creditRates.Medium;
        } else if (creditRating > 6.5 && creditRating <= 8) {
            rate = creditRates.Good;
        } else {
            rate = creditRates.VeryGood;
        }
        return rate;
    }

    static void approvedCredit(Credit credit) {
        Costumer costumer = credit.getCostumer();
        Account account = costumer.getAccount();
        account.insertCredit(credit);
    }

    public static Account getAccount(long costumerNr) throws SQLException {
        Connection connection = db.getRes();
        String queryAccount = "SELECT accountNr, balance, bankCode, creditRating FROM Account WHERE costumerNr = " + costumerNr;

        long accountNr = 0;
        double balance = 0;
        String bankCode = "";
        double creditRating = 0;

        ResultSet dataSet = connection.createStatement().executeQuery(queryAccount);

        while (dataSet.next()) {
            accountNr = dataSet.getLong("accountNr");
            balance = dataSet.getDouble("balance");
            bankCode = dataSet.getString("bankCode");
            creditRating = dataSet.getDouble("creditRating");
        }
        return new Account(balance, accountNr, bankCode, creditRating);

    }

    public static void setNewCredit(Credit credit) throws SQLException {
        Connection connection = db.getRes();

        int firstWorker = freeCreditWorkerNr();
        int secondWorker = freeCreditWorkerNr();
        String query = "INSERT INTO creditList (CreditNr, workerNr_1, workerNr_2, sum, costumerNr, time, date) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?);";

        PreparedStatement statement = connection.prepareStatement(query);
        statement.setInt(1, credit.getCreditNr());
        statement.setInt(2, firstWorker);
        statement.setInt(3, secondWorker);
        statement.setDouble(4, credit.getSum());
        statement.setLong(5, credit.getCostumer().getCostumerNr());
        statement.setLong(6, credit.getTime());
        statement.setString(7, credit.getDate());
        statement.execute();

        if (credit.getSum() > 500000.0) {
            String query2 = "UPDATE creditList SET ceo = " + supervisor.getWorkerNr() + " WHERE CreditNr = " + credit.getCreditNr();
            statement = connection.prepareStatement(query2);
            statement.execute();
        }
        statement.close();
        connection.close();

    }

    public static int freeCreditWorkerNr() throws SQLException {
        Connection connection = db.getRes();

        String workerQuery = "SELECT * FROM creditWorkers ORDER BY credits LIMIT 1;";
        ResultSet statement = connection.createStatement().executeQuery(workerQuery);

        int workerNr = 0;
        int credits = 0;
        while (statement.next()) {
            workerNr = statement.getInt("workerNr");
            credits = statement.getInt("credits");
        }
        // Update creditsNr
        String implementQuery = "UPDATE creditWorkers SET credits = " + ++credits + " WHERE workerNr = " + workerNr;
        PreparedStatement newStatement = connection.prepareStatement(implementQuery);

        newStatement.execute();
        statement.close();
        newStatement.close();
        connection.close();

        return workerNr;
    }

    public static void creditWay(double sum, Costumer costumer, long time, String date) throws SQLException, IOException {
        Credit credit = new Credit(sum, costumer, time, date);
        CreditDecision.decideCredit(credit);
    }

    @Override
    public void workerDecide(Credit credit) {
        System.out.println("Credit goes to worker for decision.");
    }
}
