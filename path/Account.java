package bank.path;

import java.util.ArrayList;

public class Account {
    private static long accountCounter = 1000000000000000L;
    double balance;
    long accountNr;
    String bankCode;
    double creditRating;
    double creditSum;
    long costumerNr;

    Costumer costumer;
    Bank bank;
    ArrayList<String> creditHist = new ArrayList<>();


    public Account(String bankCode){
        this.balance = 0;
        this.accountNr = accountCounter++;
        this.bankCode = bankCode;
        this.creditRating = 1000;
        this.creditSum = 0;
    }

    public Account(double balance, long accountNr, String bankCode, double creditRating) {
        this.balance = balance;
        this.accountNr = accountNr;
        this.bankCode = bankCode;
        this.creditRating = creditRating;
        this.creditSum = 0;
    }

    public Costumer getCostumer() {
        return costumer;
    }

    public long getAccountNr() {
        return accountNr;
    }

    public double getBalance() {
        return balance;
    }

    public double getCreditRating() {
        return creditRating;
    }

    public void setCostumer(Costumer costumer) {
        this.costumer = costumer;
    }

    public void setBank(Bank bank){
        this.bank = bank;
    }

    public void setCostumerNr(long costumerNr) {
        this.costumerNr = costumerNr;
    }

    public void insertCredit(Credit credit) {
        setCreditSum(credit.getSum(), credit.toString());
    }


    private void setCreditSum(double sum, String info) {
        this.creditSum = sum;
        this.creditHist.add(info + "approved");
    }

}
