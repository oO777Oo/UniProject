package bank.path;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;


public class Bank {

    private static final Bank bank = new Bank();
    private static int costumerNr = 0;
    private static int workerCounter = 0;
    private static final String root = "root";
    private static final String rootPass = "";
    private static final String connectUrl = "jdbc:mysql://localhost:3306/AppDev";
    private static final String timeZone = "?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";
    private static final String driverLink = "com.mysql.cj.jdbc.Driver";
    static final DBConnection db = new DBConnection(root, rootPass,connectUrl + timeZone, driverLink);
    static String bankCode = "BLZBLZ";
    static Worker ceo;
    static Worker supervisor;
    static ArrayList<Credit> creditWaitingList = new ArrayList<>();
    
    private Bank() { }

    /**
     * =======================================================================================================
     *                                         SELECT FROM DB.
     * */

    public static Costumer getCostumerData(long costumerNr) throws SQLException {

        Connection connection = db.getRes();
        String queryCostumer = "SELECT firstName, lastName, birthday, address FROM costumerData WHERE costumerNR = ";
        ResultSet dataSetCostumer = connection.createStatement().executeQuery(queryCostumer + costumerNr + ";");
        // Costumer data
        String firstName = "";
        String lastName = "";
        String birthday = "";
        String address = "";
        while (dataSetCostumer.next()) {
            firstName = dataSetCostumer.getString("firstName");
            lastName = dataSetCostumer.getString("lastName");
            birthday = dataSetCostumer.getString("birthday");
            address = dataSetCostumer.getString("address");
        }
        return new Costumer(firstName, lastName, birthday, address, costumerNr);
    }


    public Account getAccountData(long accountNr) throws SQLException {
        Connection connection = db.getRes(); // DB connection

        // SQL SELECT Queries
        String queryAccount = "SELECT costumerNr, balance, bankCode, creditRating FROM Account WHERE accountNr = " + accountNr;

        long costumerNr = 0;
        double balance = 0;
        String bankCode = "";
        double creditRating = 0;

        // Next Implementation of Account query
        ResultSet dataSet = connection.createStatement().executeQuery(queryAccount);

        while (dataSet.next()) {
            costumerNr = dataSet.getLong("costumerNr");
            balance = dataSet.getDouble("balance");
            bankCode = dataSet.getString("bankCode");
            creditRating = dataSet.getDouble("creditRating");
        }
        Account account = new Account(balance, accountNr, bankCode, creditRating);
        account.setCostumerNr(costumerNr);
        return account;
    }

    public Costumer wholeCostumerAccountData(long accountNr) throws SQLException {
        Account account = getAccountData(accountNr);
        Costumer costumer = getCostumerData(account.getCostumer().getCostumerNr());
        costumer.setAccount(account);
        account.setCostumer(costumer);
        return costumer;
    }

    public static Worker getWorker(int workerNr) throws SQLException {
        Connection connection = db.getRes();
        String query = "SELECT status, salary, date, sex, firstName, lastName, birthday, address FROM Workers" +
                " WHERE workerNr = " + workerNr + ";";

        String status = "";
        double salary = 0;
        String date = "";
        boolean sex = false;
        String firstName = "";
        String lastName = "";
        String birthday = "";
        String address = "";

        ResultSet dataSetCostumer = connection.createStatement().executeQuery(query);
        while (dataSetCostumer.next()) {
            status = dataSetCostumer.getString("status");
            salary = dataSetCostumer.getDouble("salary");
            date = dataSetCostumer.getString("date");
            sex = dataSetCostumer.getBoolean("sex");
            firstName = dataSetCostumer.getString("firstName");
            lastName = dataSetCostumer.getString("lastName");
            birthday = dataSetCostumer.getString("birthday");
            address = dataSetCostumer.getString("address");
        }
        dataSetCostumer.close();
        connection.close();
        Worker worker = new Worker(firstName,lastName,birthday,address,status,sex,salary, date);
        worker.setWorkerNr(workerNr);
        return worker;

    }

    /**
     * ========================================================================================================
     *                                  INSERT IN DB.
     * */

    public static void addNewCostumer(String firstName, String lastName, String birthday, String address) throws SQLException {
        Account account = new Account(bankCode);
        account.setBank(bank);

        Connection connection = db.getRes();

        String queryCostumer = "INSERT INTO costumerData (costumerNr, firstName, lastName, birthday, address)" +
                "VALUES (?, ?, ?, ?, ?)";

        String queryAccount = "INSERT INTO Account (accountNr, costumerNr, balance, bankCode, creditRating)" +
                "VALUES (?, ?, ?, ?, ?)";

        PreparedStatement statement = connection.prepareStatement(queryCostumer);
        statement.setLong(1, costumerNr);
        statement.setString(2, firstName);
        statement.setString(3, lastName);
        statement.setString(4, birthday);
        statement.setString(5, address);
        statement.execute();

        statement = connection.prepareStatement(queryAccount);
        statement.setLong(1, account.accountNr);
        statement.setLong(2, costumerNr);
        statement.setDouble(3, account.balance);
        statement.setString(4, account.bankCode);
        statement.setDouble(5, account.creditRating);
        statement.execute();

        statement.close();
        connection.close();
        costumerNr ++;
    }

    public void addNewWorker(String firstName, String lastName, String birthday, String address, String status, Boolean sex, double salary) throws SQLException {
        Connection connection = db.getRes();
        Date date = new Date();
        String query = "INSERT INTO Workers (workerNr, status, salary, date, sex, firstName, lastName, birthday, address)" +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?);";

        String queryCredit = "INSERT INTO creditWorkers(workerNr) VALUES (?);";

        PreparedStatement statement = connection.prepareStatement(query);
        statement.setInt(1, Bank.workerCounter);
        statement.setString(2, status);
        statement.setDouble(3, salary);
        statement.setString(4, date.toString());
        statement.setBoolean(5, sex);
        statement.setString(6, firstName);
        statement.setString(7, lastName);
        statement.setString(8, birthday);
        statement.setString(9,address);
        statement.execute();

        switch (status) {
            case "credit-worker":
                statement = connection.prepareStatement(queryCredit);
                statement.setInt(1, Bank.workerCounter);
                statement.execute();
                break;
            case "ceo":
                ceo = createWorker(firstName, lastName, birthday, address, status, sex, salary, date.toString());
                break;
            case "supervisor":
                supervisor = createWorker(firstName, lastName, birthday, address, status, sex, salary, date.toString());
                break;
        }

        statement.close();
        connection.close();
        workerCounter ++;
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

    /** ========================================================================================================
     *                                              DELETE FROM DB.
     * */






    /**
     * Till hier is DB method
     *
     * Next exercise Method!
     * */
    public static Credit creditWay(double sum, Costumer costumer, long time, String date) throws SQLException {
        Credit credit = new Credit(sum, costumer, time, date);
        setNewCredit(credit);

        return credit;
    }


    /**
     * This methods need to change.
     * */

    private static Worker createWorker(String firstName, String lastName, String birthday, String address, String status, Boolean sex, double salary,String date){
        /** Create worker for Ceo or supervisor! */
        return new Worker(firstName, lastName, birthday, address, status, sex, salary, date);
    }

    public static String getBankCode() {
        return bankCode;
    }

    /** Duplicates methods  */

}
