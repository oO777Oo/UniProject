package Client;

import java.io.Serializable;

public class Account implements Serializable {

    private long costumerNr;
    private long accountNr;
    private double balance;
    private double creditRating;
    private double creditSum;

    private String creditStatus;

    public Account() {
    }

    public long getCostumerNr() {
        return costumerNr;
    }

    public void setCostumerNr(long costumerNr) {
        this.costumerNr = costumerNr;
    }

    public long getAccountNr() {
        return accountNr;
    }

    public void setAccountNr(long accountNr) {
        this.accountNr = accountNr;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public double getCreditRating() {
        return creditRating;
    }

    public void setCreditRating(double creditRating) {
        this.creditRating = creditRating;
    }

    public double getCreditSum() {
        return creditSum;
    }

    public void setCreditSum(double creditSum) {
        this.creditSum = creditSum;
    }

    public void setCredit(Credit credit) {
        this.creditSum = credit.getSum();
    }

    public String getCreditStatus() {
        return creditStatus;
    }

    public void setCreditStatus(String creditStatus) {
        this.creditStatus = creditStatus;
    }
}
