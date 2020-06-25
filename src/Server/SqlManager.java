package Server;

import Client.Account;
import Client.Credit;
import Client.DbData;
import Client.User;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

public class SqlManager extends UnicastRemoteObject implements QueriesImp, Serializable {
    /**
     * remote class SqlManager
     * implements QueriesImp interface
     */
    public SqlManager() throws RemoteException {
    }

    static MySqlManagement mySqlManagement; // db obj initialized

    @Override
    public Account selectAccountQuery(Credit credit) throws RemoteException {
        /** Method to select Account data from db where cond=condValue.
         * @param cond: Select condition
         * @param condValue: condition value
         * @param data:  refer to SELECT data witch need to be SELECTED
         * @return account: object type Account with all data from data parameter*/

        String query = "SELECT * FROM Account where costumerNr = " + credit.getCostumerNr();
        Account account = new Account(); // empty account initialization
        try (Connection connection = mySqlManagement.connectionToDB()) {
            ResultSet dataSet = connection.createStatement().executeQuery(query); // Getting data from db
            while (dataSet.next()) {
                account.setCostumerNr(dataSet.getLong("costumerNr"));
                account.setBalance(dataSet.getDouble("balance"));
                account.setAccountNr(dataSet.getLong("accountNr"));
                account.setCreditRating(dataSet.getDouble("creditRating"));
                account.setCreditSum(dataSet.getDouble("creditSum"));
            }
        } catch (SQLException exception) {
            /*Catch block to catch the error!*/
            exception.printStackTrace();
        }

        return account; // return account obj type Account with selected data from Db.
    }

    @Override
    public String insertStringBuilder(String table) {
        /** Construct INSERT String query for param table and return it
         * @param table: Show query table
         * @return String
         * */
        String query = "INSERT INTO " + table;
        if (table.equals("Credit")) { // Insert for Credit table
            query += "(CreditNr, sum, costumerNr, start, end) VALUES (?,?,?,?,?)";
            return query;
        } else if (table.equals("Account")) { // Insert for Account table
            query += "(balance, accountNr, creditRating, creditSum, costumerNr) VALUES (?, ?, ?, ?, ?)";
            return query;
        } else if (table.equals("Costumer")) { // Insert for Costumer table
            query += "(costumerNr, firstName, lastName, birthday, address) VALUES (?, ?, ?, ?, ?)";
            return query;
        }
        return query;
    }

    @Override
    public User selectUserQuery(Credit credit) {
        /** Method to select User data from db where cond = condValue!
         * @param cond: Select condition
         * @param condValue: condition value
         * @param data:  refer to SELECT data witch need to be SELECTED
         * @return user: object type User with all data from data parameter.*/
        User user = new User(); // Construct user obj with no data!
        String query = "SELECT * FROM Costumer WHERE costumerNr = " + credit.getCostumerNr();

        try (Connection connection = mySqlManagement.connectionToDB()) {
            ResultSet dataSet = connection.createStatement().executeQuery(query); // Getting data from db
            while (dataSet.next()) {
                user.setCostumerNr(dataSet.getLong("costumerNr"));
                user.setAddress(dataSet.getString("address"));
                user.setBirthday(dataSet.getString("birthday"));
                user.setFirstName(dataSet.getString("firstName"));
                user.setLastName(dataSet.getString("lastName"));
            }
        } catch (SQLException exception) {
            /*Catch block to catch the error!*/
            exception.printStackTrace();
        }
        return user; // return obj with User type with data from Db.
    }

    @Override
    public void insertCreditQuery(int creditNr, double sum, Long costumerNr, Date start, Date end) {
        /** Insert Credit data in Db
         * @param creditNr: Credit number.
         * @param sum: Credit sum.
         * @param costumerNr: Costumer number.
         * @param start: Credit start date type(Date)
         * @param end: Credit end date type(Date)
         * @return void!
         * */
        String insertQuery = insertStringBuilder("Credit"); // use insertStringBuilder method to create sql query
        try (Connection connection = mySqlManagement.connectionToDB()) { // try connection
            PreparedStatement statement = connection.prepareStatement(insertQuery); // construct query
            statement.setInt(1, creditNr);
            statement.setDouble(2, sum);
            statement.setLong(3, costumerNr);
            // Date is changed from type Date to type String
            String date = (start.getYear() + 1900) + "-" + start.getMonth() + "-" + start.getDate();
            /* start.getYear() + 1900 because Date class start at year 1900 that's why is this + needed*/
            statement.setString(4, date);
            date = (end.getYear() + 1900) + "-" + end.getMonth() + "-" + end.getDate();
            statement.setString(5, date);
            statement.execute();
        } catch (SQLException throwable) {
            throwable.printStackTrace();
        }
    }

    @Override
    public void updateStatus(Account account) {
        /** update credit status which is Worker answer!
         * @param account: Obj (Account) from which this status is taken
         * @return void
         * */
        String query = "UPDATE Account SET creditStatus = ' " + account.getCreditStatus() + "'" + " WHERE accountNR =" + account.getAccountNr();
        try (Connection connection = mySqlManagement.connectionToDB()) {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.execute();
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    @Override
    public void autoCreditApproved(Credit credit) {
        /** Method for automatically approved is called when credit status from user is bigger as his credit sum
         * Method the same works when credit sum is bigger as 500.000,00
         * @param credit: Obj type(Credit)
         * @return void
         * */
        String query = "INSERT INTO Credit (CreditNr, sum, costumerNr, start, end, firstAns, secondAns, supervisorAns, ceoAns) VALUES (?,?,?,?,?,?,?,?,?)";
        try (Connection connection = mySqlManagement.connectionToDB()) {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, credit.getCreditNr());
            statement.setDouble(2, credit.getSum());
            statement.setLong(3, credit.getCostumerNr());
            String date = (credit.getStart().getYear() + 1900) + "-" + credit.getStart().getMonth() + "-" + credit.getStart().getDate();
            statement.setString(4, date);
            date = (credit.getEnd().getYear() + 1900) + "-" + credit.getEnd().getMonth() + "-" + credit.getEnd().getDate();
            statement.setString(5, date);
            statement.setShort(6, (short) 1);
            statement.setShort(7, (short) 1);
            statement.setShort(8, (short) 1);
            statement.setShort(9, (short) 1);
            statement.execute();

        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    @Override
    public MySqlManagement getMySql() {
        return mySqlManagement;
    }

    @Override
    public void setMySqlParamName(DbData data) {
        /**
         * Method to init data for db connection!
         * @param data: Obj type(DbData) has only 4 arguments:
         *            String name -> db name
         *            String pass -> db pass
         *            String url -> db url
         *            String initUrl -> initial db url
         * */
        mySqlManagement = new MySqlManagement(data.getName(), data.getPass(), data.getUrl(), data.getInitUrl());

    }

    @Override
    public Connection getConnection() {
        /**
         * Intermediate method for connection:
         * Use mySqlManagement.connectionToDB
         * @return mySqlManagement or null;
         * */
        try {
            return mySqlManagement.connectionToDB();
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return null;
    }


}
