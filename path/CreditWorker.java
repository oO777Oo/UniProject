package bank.path;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class CreditWorker extends Worker {
    /** checkWorkerNr -> Arata pe ce pozitie este lucratorul */

    long workerNr;

    public CreditWorker(String firstName, String lastName, String birthday, String address, String status, Boolean sex, double salary, String date, long workerNr) {
        super(firstName, lastName, birthday, address, status, sex, salary, date);
        this.workerNr = workerNr;
    }

    public void doWork() throws SQLException, IOException {
        Connection connection = Bank.db.getRes();
        ArrayList<Credit> work = workList(connection);
        StringBuilder query = new StringBuilder("UPDATE creditList SET ");
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        for (Credit credit: work) {
            System.out.println("Do you want to make next credit document? Y/N \n");
            String ans = reader.readLine();
            if (ans.equals("N")) {
                connection.close();
                break;
            } else {
                decideCredit(credit, connection);
                String currentWorkerNr = checkWorkerNr(credit, connection);
                query.append(currentWorkerNr).append(" = -1 WHERE CreditNr = ").append(credit.getCreditNr());
                PreparedStatement statement = connection.prepareStatement(query.toString());
                statement.execute();
                statement.close();
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

    public void decideCredit(Credit credit, Connection connection) throws SQLException, IOException {
        creditRates rate;
        double percentRate = 0;

        String getAccount = "SELECT * FROM Account WHERE costumerNr = " + credit.getCostumerNr() + " ;";

        long accountNr = 0;
        double balance = 0;
        double creditRating = 0;

        ResultSet data = connection.createStatement().executeQuery(getAccount);

        while (data.next()) {
            accountNr = data.getLong("accountNr");
            balance = data.getDouble("balance");
            creditRating = data.getDouble("creditRating");
        }
        data.close();

        if (creditRating <= 5) {
            rate = creditRates.Bad;
        } else if (creditRating > 5 && creditRating <= 6.5) {
            rate = creditRates.Medium;
        } else if (creditRating > 6.5 && creditRating <= 8) {
            rate = creditRates.Good;
        } else {
            rate = creditRates.VeryGood;
        }

        switch (rate) {
            case Bad:
                percentRate = 15.0;
            case Medium:
                percentRate = 10.0;
            case Good:
                percentRate = 6.0;
            case VeryGood:
                percentRate = 3.0;
        }
        System.out.println("Heir are all data about costumer: \n");
        System.out.println("account number: " + accountNr);
        System.out.println("balance: " + balance);
        System.out.println("credit rating: " + creditRating);
        System.out.println("Case rate is " + rate);
        boolean ans = insertAnswer();
        insertInDb(ans, percentRate, credit, connection);

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
    /** Automatiesch ohne Buffer */
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
    /** ============================ Help Methods to solve code duplication in Methods ============================ */

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
}
