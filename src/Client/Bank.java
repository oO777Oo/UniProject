package Client;

import javax.naming.NamingException;
import javax.naming.InitialContext;
import java.text.SimpleDateFormat;
import java.rmi.RemoteException;
import java.text.DecimalFormat;
import javax.naming.Context;
import java.util.Stack;

import Server.*;

import java.sql.*;
import java.io.*;

public class Bank implements Serializable {
    /**
     * class Singleton pattern
     * because we use only one obj bank in whole program and
     * bank has the same methods for all users, workers and the same db
     * for all humans
     * <p>
     * Singleton pattern -> private Constructor and final static obj initialized as argument!
     */
    private final static Bank bk = new Bank();
    public static int creditNr = 1;

    private Bank() {
    }

    public static void getCreditApplication(Credit credit, boolean direction) throws NamingException, RemoteException {
        /** Method work remotely with QueriesImp on rmi://localhost/query
         * 1. direction show us if this credit need to be verified from workers or not
         * if not is automatically approved.
         * @param credit: Credit obj
         * @param direction: direction type(boolean)
         * */
        credit.setCreditNr(creditNr);
        creditNr++;
        Context context = new InitialContext();
        QueriesImp queriesImp = (QueriesImp) context.lookup("rmi://localhost/query");
        if (!direction) {
            if (credit.checkSuperCredit()) {
                credit = new SuperCredit(credit);
            }
            queriesImp.insertCreditQuery(credit.getCreditNr(), credit.getSum(),
                    credit.getCostumerNr(), credit.getStart(), credit.getEnd());
        } else {
            queriesImp.autoCreditApproved(credit);
        }
    }


    public static StringBuilder answer(Credit credit) {
        /** Method return answer for credit application if application is positive
         * this method call another method timeCalculation which return sum which need
         * to be payed every month.
         * @return creditStatus: String with ans pattern
         * */
        String pattern = "dd-MM-yyyy";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        StringBuilder creditStatus = new StringBuilder("Your credit application is approved!\n");
        creditStatus.append("You become ").append(credit.getSum()).append(" Euro.\n");
        creditStatus.append("Credit time is from ").append(simpleDateFormat.format(credit.getStart()));
        creditStatus.append(" till ").append(simpleDateFormat.format(credit.getEnd())).append("\n");
        creditStatus.append("Month payment will be: ").append(timeCalculation(credit));
        return creditStatus;
    }

    private static String timeCalculation(Credit credit) {
        /** Method calculate sum which need to be payed every month.
         * @param credit: Credit obj
         * @return decimalFormat of double type
         * */
        int years = credit.getEnd().getYear() - credit.getStart().getYear();
        int months = credit.getStart().getMonth() - credit.getEnd().getMonth();
        if (months < 0) {
            months *= -1;
        }
        months += years * 12;
        double newSum = (credit.getSum() * (100 + credit.getPercentRange())) / 100;
        return new DecimalFormat("#.##").format(newSum / months);
    }


    public static void workingMethod(Worker worker) throws RemoteException, NamingException {
        /**Method is method were worker need to give answer from console if they approve credit or not
         * Method is generalized for all Workers which work in Credit direction!
         * This Method is central point for credit decision
         * @param worker: type Worker
         * */
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        Context context = new InitialContext();
        BankSystemActivity queriesImp = (BankSystemActivity) context.lookup("rmi://localhost/work");
        Stack<Credit> work = queriesImp.doWork(worker);
        while (!work.isEmpty()) {
            Credit credit = work.pop();
            Account account = infoPrint(credit);

            try {
                String ans;
                short helper = 0;
                System.out.println("Do you want to accept it? y/n");
                ans = reader.readLine();
                if (ans.toLowerCase().equals("y")) {
                    helper = 1;
                }
                /* come ans from console */
                if (worker instanceof CreditWorker) {
                    if (credit.getFirstAns() < 0) {
                        credit.setFirstAns(helper);
                        setAnswerInDb(credit, true);
                    } else {
                        credit.setSecondAns(helper);
                        setAnswerInDb(credit, false);
                    }
                } else if (worker instanceof SupervisorWorker) {
                    credit.setSupervisorAns(helper);
                    setAnswerInDbSupervisor(credit);
                } else if (worker instanceof CeoWorker && credit.getSum() >= 500000) {
                    SuperCredit superCredit = new SuperCredit(credit);
                    superCredit.setCeoAns(helper);
                    setAnswerInDbCeo(superCredit);
                } // If condition with instanceof to determine which worker gave the ans
                checkAnswers(credit, account, helper); // Check if is not the last needed ans
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }
    }

    public static void setAnswerInDb(Credit credit, boolean flag) throws NamingException, RemoteException {
        /**
         * Method which start remote method from BankSystemActivity!
         * Set ans from CreditWorker in db
         * @param credit: type Credit
         * @param flag: type boolean
         * */
        Context context = new InitialContext();
        BankSystemActivity queriesImp = (BankSystemActivity) context.lookup("rmi://localhost/work");
        queriesImp.setAnswerInDb(credit, flag);
    }

    public static void setAnswerInDbSupervisor(Credit credit) throws NamingException, RemoteException {
        /**
         * Method which start remote method from BankSystemActivity!
         * Set ans from Supervisor in db
         * @param credit: type Credit
         * */
        Context context = new InitialContext();
        BankSystemActivity queriesImp = (BankSystemActivity) context.lookup("rmi://localhost/work");
        queriesImp.setAnswerInDbSupervisor(credit);

    }

    public static void setAnswerInDbCeo(SuperCredit credit) throws NamingException, RemoteException {
        /**
         * Method which start remote method from BankSystemActivity
         * Set ans from ceo in db
         * */
        Context context = new InitialContext();
        BankSystemActivity queriesImp = (BankSystemActivity) context.lookup("rmi://localhost/work");
        queriesImp.setAnswerInDbCeo(credit);

    }

    public static void checkAnswers(Credit credit, Account account, short ans) throws NamingException, RemoteException {
        /**
         * Check Method -> control if credit has all needed ans
         * if yes calculate the ans is positive or not and set status
         * @param credit: type Credit
         * @param account: type Account
         * @param ans: type short
         * */
        Context context = new InitialContext();
        QueriesImp queriesImp = (QueriesImp) context.lookup("rmi://localhost/query");
        if (credit.getFirstAns() != -1 && credit.getSecondAns() != -1 && credit.getSupervisorAns() != -1) {
            if (credit.getSum() < 500000) {
                int totalAns = credit.getFirstAns() + credit.getSecondAns() + credit.getSupervisorAns();
                if (totalAns >= 2) {
                    String answer = answer(credit).toString();
                    account.setCreditStatus(answer);
                    account.setCreditSum(credit.getSum());
                    updateCreditSum(account, credit);
                } else {
                    account.setCreditStatus("Your credit is declined!");
                }
                queriesImp.updateStatus(account);
            } else {
                SuperCredit superCredit = new SuperCredit(credit, ans);
                if (superCredit.getCeoAns() != -1) {
                    int totalAns = superCredit.getFirstAns() + superCredit.getSecondAns() + superCredit.getSupervisorAns() + superCredit.getCeoAns();
                    if (totalAns >= 3 || (totalAns == 2 && superCredit.getCeoAns() == 1)) {
                        String answer = answer(credit).toString();
                        account.setCreditStatus(answer);
                        account.setCreditSum(credit.getSum());

                        queriesImp.updateStatus(account);
                        updateCreditSum(account, credit);
                    } else {

                        queriesImp.updateStatus(account);
                        account.setCreditStatus("Your credit is declined!");
                    }
                }
            }

        }
    }

    private static void updateCreditSum(Account account, Credit credit) throws NamingException, RemoteException {
        /**
         * Method which start remote method from BankSystemActivity
         * update credit sum in account
         * Method works only if credit request is approved
         * @param account: type Account
         * @param credit: type Credit
         * */
        Context context = new InitialContext();
        BankSystemActivity queriesImp = (BankSystemActivity) context.lookup("rmi://localhost/work");
        queriesImp.updateCreditSum(account, credit);

    }

    private static Account infoPrint(Credit credit) throws NamingException, RemoteException {
        /**
         * Method which print out all info about credit, user and conto from user
         * @param credit: type Credit
         * @return account: type Account
         * */
        Context context = new InitialContext();
        QueriesImp queriesImp = (QueriesImp) context.lookup("rmi://localhost/query");
        User user = queriesImp.selectUserQuery(credit);
        Account account = queriesImp.selectAccountQuery(credit);
        System.out.println("Credit information about " + user.getFirstName() + " " + user.getLastName() + ".");
        System.out.println("Live at " + user.getAddress() + " and born on " + user.getBirthday());
        System.out.println("Credit in sum of " + credit.getSum() + " with credit rating " + account.getCreditRating() + ".");
        System.out.println("Till " + credit.getEnd());
        return account;
    }

    private static void dropDbAppDev() throws NamingException {
        /**
         * Method DropDbAppDev to be able again to test the project
         * */
        Context context = new InitialContext();
        QueriesImp queriesImp = (QueriesImp) context.lookup("rmi://localhost/query");
        String query = "DROP SCHEMA AppDev;";
        String query1 = "DROP TABLE Costumer";
        String query2 = "DROP TABLE Account;";
        String query3 = "DROP TABLE Credit";
        try (Connection connection = queriesImp.getConnection()) {

            Statement statement = connection.createStatement();
            statement.execute(query3);
            statement.execute(query2);
            statement.execute(query1);
            statement.execute(query);
        } catch (SQLException | RemoteException exception) {
            exception.printStackTrace();
        }
    }
}
