package Server;

import Client.*;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Stack;

public class BankSystem extends UnicastRemoteObject implements BankSystemActivity {
    /**
     * Remote class BankSystem implements interface BankSystemActivity.
     * This Interface has all functionality for a Bank which work remotely
     */
    static MySqlManagement mySqlManagement; // Static db

    public BankSystem() throws RemoteException {
    }

    public static void setMySqlManagement(MySqlManagement sqlManagement) {
        mySqlManagement = sqlManagement;
    }

    @Override
    public Stack<Credit> doWork(Worker worker) {
        /** This method select from db all credits which has no worker ans
         * it's used stack to allocate memory logically
         * push them in Stack and return it
         * @param worker: Obj type(Worker)
         * @return workList: Obj type(Stack) with all Credits objects
         * */
        int creditNr;
        double sum;
        long costumerNr;
        Date start;
        Date end;
        double percentRange;
        short firstAns;
        short secondAns;
        short supervisorAns;
        short ceoAns;
        String helper;
        String query = "";
        Stack<Credit> workList = new Stack<>(); // Stack initialization!

        if (worker instanceof CreditWorker) { // instanceof to check which worker class is used
            query = "SELECT * FROM Credit WHERE firstAns=-1 OR secondAns=-1";
        } else if (worker instanceof SupervisorWorker) {
            query = "SELECT * FROM Credit WHERE supervisorAns=-1";
        } else if (worker instanceof CeoWorker) {
            query = "SELECT * FROM Credit WHERE ceoAns=-1 AND sum >= 500000";
        }
        try (Connection connection = mySqlManagement.connectionToDB()) {
            ResultSet data = connection.createStatement().executeQuery(query);
            while (data.next()) {
                creditNr = data.getInt("creditNr");
                sum = data.getDouble("sum");
                costumerNr = data.getLong("costumerNr");
                helper = data.getString("start");
                start = new SimpleDateFormat("yyyy-MM-dd").parse(helper);
                helper = data.getString("end");
                end = new SimpleDateFormat("yyyy-MM-dd").parse(helper);
                percentRange = data.getDouble("percentRange");
                firstAns = data.getShort("firstAns");
                secondAns = data.getShort("secondAns");
                supervisorAns = data.getShort("supervisorAns");
                ceoAns = data.getShort("ceoAns");

                Credit credit = new Credit(creditNr, sum, costumerNr, start, end, firstAns, secondAns, supervisorAns);
                credit.setPercentRange(percentRange);

                if (sum > 500000) {
                    // condition to create SuperCredit because sum > 500,000.00
                    credit = new SuperCredit(credit, ceoAns);
                }
                workList.push(credit);
            }
        } catch (SQLException | ParseException exception) {
            exception.printStackTrace();
        }
        return workList;
    }

    @Override
    public void setAnswerInDb(Credit credit, boolean flag) {
        /** Insert worker ans in db flag help to decide which answer is it first or second
         * @param credit: Credit obj
         * @param flag: boolean
         * */
        try (Connection connection = mySqlManagement.connectionToDB()) {
            String query = "UPDATE Credit SET ";
            if (flag) {
                query = query + "firstAns=" + credit.getFirstAns() + " WHERE CreditNr=" + credit.getCreditNr();
            } else {
                query = query + "secondAns=" + credit.getSecondAns() + " WHERE CreditNr=" + credit.getCreditNr();
            }
            PreparedStatement statement = connection.prepareStatement(query);
            statement.execute();

        } catch (SQLException exception) {
            exception.printStackTrace();
        }

    }

    @Override
    public void setAnswerInDbSupervisor(Credit credit) {
        /** Method insert answer from SuperVisor in db
         * @param credit: Credit obj
         * */
        try (Connection connection = mySqlManagement.connectionToDB()) {
            String query = "UPDATE Credit SET ";
            query = query + "supervisorAns=" + credit.getSupervisorAns() + " WHERE CreditNr=" + credit.getCreditNr();
            PreparedStatement statement = connection.prepareStatement(query);
            statement.execute();
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    @Override
    public void setAnswerInDbCeo(SuperCredit credit) {
        /** Method insert answer from ceo in db
         * @param credit: SuperCredit obj
         * */
        try (Connection connection = mySqlManagement.connectionToDB()) {
            String query = "UPDATE Credit SET ";
            query = query + "ceoAns=" + credit.getCeoAns() + " WHERE CreditNr=" + credit.getCreditNr();
            PreparedStatement statement = connection.prepareStatement(query);
            statement.execute();

        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    @Override
    public void updateCreditSum(Account account, Credit credit) {
        /** Method insert credit sum in account table if credit is approved
         * @param account: Account obj
         * @param credit: Credit obj
         * */
        try (Connection connection = mySqlManagement.connectionToDB()) {
            String query = "UPDATE Account SET ";
            query = query + "creditSum=" + credit.getSum() + " WHERE accountNr=" + account.getAccountNr();
            PreparedStatement statement = connection.prepareStatement(query);
            statement.execute();
        } catch (SQLException exception) {
            exception.printStackTrace();
        }

    }
}
