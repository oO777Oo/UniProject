package bank.path;


public class Account {
    private static long accountCounter = 1000000000000000L;
    double balance;
    long accountNr;
    String bankCode;
    double creditRating;
    long costumerNr;
    Costumer costumer;
    Bank bank;

    public Account(String bankCode){
        this.balance = 0;
        this.accountNr = accountCounter++;
        this.bankCode = bankCode;
        this.creditRating = 1000;
    }

    public Account(double balance, long accountNr, String bankCode, double creditRating) {
        this.balance = balance;
        this.accountNr = accountNr;
        this.bankCode = bankCode;
        this.creditRating = creditRating;
    }

    public Costumer getCostumer() {
        return costumer;
    }

    public void setCostumer(Costumer costumer) {
        this.costumer = costumer;
    }


    public double getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }

    public long getAccountNr() {
        return accountNr;
    }

    public void setAccountNr(long accountNr) {
        this.accountNr = accountNr;
    }

    public String getBankCode() {
        return bankCode;
    }

    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }

    public double getCreditRating() {
        return creditRating;
    }

    public void setCreditRating(int creditRating) {
        this.creditRating = creditRating;
    }

    public void setBank(Bank bank){
        this.bank = bank;
    }

    public void setCostumerNr(long costumerNr) {
        this.costumerNr = costumerNr;
    }
}
