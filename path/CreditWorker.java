package bank.path;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class CreditWorker extends Worker implements CreditDecision {
    /** checkWorkerNr -> Arata pe ce pozitie este lucratorul */

    long workerNr;

    public CreditWorker(String firstName, String lastName, String birthday, String address, String status, Boolean sex, double salary, String date, long workerNr) {
        super(firstName, lastName, birthday, address, status, sex, salary, date);
        this.workerNr = workerNr;
    }

    public void doWork() throws SQLException, IOException {
        Connection connection = Bank.db.getRes();
        ArrayList<Credit> work = workList(connection);
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        for (Credit credit: work) {
            System.out.println("Do you want to make next credit document? Y/N \n");
            String ans = reader.readLine();
            if (ans.equals("N")) {
                connection.close();
                break;
            } else {
                workerDecide(credit);
                work.remove(credit);
            }
        }
        System.out.println("Have a nice day!");
    }


    private ArrayList<Credit> workList(Connection connection) throws SQLException {
        ArrayList<Credit> workToDo = new ArrayList<>();

        String query = "SELECT * FROM creditList WHERE workerNr_1 = " + workerNr + " OR workerNr_2 = " + workerNr + " OR ceo = " + workerNr + ";";
        int creditNr;
        double sum;
        long costumerNr;
        long time;
        String date;

        ResultSet data = connection.createStatement().executeQuery(query);
        while (data.next()) {
            creditNr = data.getInt("CreditNr");
            sum = data.getDouble("sum");
            costumerNr = data.getLong("costumerNr");
            time = data.getLong("time");
            date = data.getString("date");
            Credit credit = new Credit(creditNr, sum, costumerNr, time, date);
            workToDo.add(credit);
        }
        data.close();

        return workToDo;
    }


    private void insertInDb(boolean ans, double percentRate, Credit credit, Connection connection) throws SQLException {
        String currentWorkerNr = checkWorkerNr(credit, connection);
        String ansQuery = "UPDATE creditList SET ";
        String ratesQuery = ansQuery + "percentRate = " + percentRate + " ;";

        switch (currentWorkerNr) {
            case "workerNr_1":
                ansQuery = ansQuery + "status_1 = ";
                break;
            case "workerNr_2":
                ansQuery = ansQuery + "status_2 = ";
                break;
            case "ceo":
                ansQuery = ansQuery + "ceo_status = ";
                break;
        }

        ansQuery = ansQuery + ans + " WHERE CreditNr = " + credit.getCreditNr() + " ;" ;

        PreparedStatement newStatement = connection.prepareStatement(ansQuery);
        newStatement.execute();

        newStatement = connection.prepareStatement(ratesQuery);
        newStatement.execute();

        newStatement.close();
        connection.close();
    }

    private Boolean insertAnswer() throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String answer;

        while (true) {
            System.out.println("Do you want to confirm this credit? \n");
            System.out.println("Write Y for yes and N for no:  ");
            answer = reader.readLine();
            System.out.println("You write " + answer + "to confirm\n Y/N");
            String confirm = reader.readLine();

            if (confirm.equals("Y")) {
                break;
            }
        }
        return answer.equals("Y");
    }

    private String checkWorkerNr(Credit credit, Connection connection) throws SQLException {
        String ans = "";
        int workerNr_1 = 0;
        int workerNr_2 = 0;
        int ceo = 0;

        String creditQuery = "SELECT workerNr_1, workerNr_2, ceo FROM creditList WHERE CreditNr = " + credit.getCreditNr() + " ;";
        ResultSet data = connection.createStatement().executeQuery(creditQuery);

        while (data.next()) {
            workerNr_1 = data.getInt("workerNr_1");
            workerNr_2 = data.getInt("workerNr_2");
            ceo = data.getInt("ceo");
        }

        data.close();
        connection.close();

        if(workerNr_1 == this.workerNr) {
            ans = "workerNr_1";
        } else if (workerNr_2 == this.workerNr) {
            ans = "workerNr_2";
        } else if (ceo == this.workerNr) {
            ans = "ceo";
        }
        return ans;

    }

    @Override
    public void workerDecide(Credit credit) throws SQLException, IOException {
        double percentRate = 0;
        Connection connection = Bank.db.getRes();
        creditRates rate = creditRate(credit, connection);
        Account account = Bank.getAccount(credit.costumerNr);
        switch (rate) {
            case Bad:
                percentRate = 15.0;
            case Medium:
                percentRate = 10.0;
        }


        System.out.println("Heir are all data about costumer: \n");
        System.out.println("account number: " + account.getAccountNr());
        System.out.println("balance: " + account.getBalance());
        System.out.println("credit rating: " + account.getCreditRating());
        System.out.println("Case rate is " + rate);
        boolean ans = insertAnswer();

        ArrayList<Object> infoList = new ArrayList<>();
        infoList.add(ans);
        infoList.add(percentRate);
        infoList.add(credit);
        Bank.creditWaitingList.add(infoList);
        deleteCreditFromDB(credit);

    }
    private void deleteCreditFromDB(Credit credit) throws SQLException {
        Connection connection = Bank.db.getRes();
        String query = "DELETE FROM creditList WHERE CreditNr = " + credit.creditNr;
        PreparedStatement statement = connection.prepareStatement(query);
        statement.execute(query);
        connection.close();
    }

    public creditRates creditRate(Credit credit, Connection connection) throws SQLException, IOException {
        return Bank.creditRate(credit, connection);
    }
}
