package bank.path;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

public class Costumer extends Human {
    long costumerNr;
    Account account;
    ArrayList<Credit> creditHistory = new ArrayList<>();

    public Costumer(String firstName, String lastName, String birthday, String address, long costumerNr, Account account){
        super(firstName,lastName, birthday, address);
        this.costumerNr = costumerNr;
        this.account = account;
    }

    public Costumer(String firstName, String lastName, String birthday, String address, long costumerNr) {
        super(firstName, lastName, birthday, address);
        this.costumerNr = costumerNr;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public long getCostumerNr() {
        return costumerNr;
    }

    public void setCostumerNr(int costumerNr) {
        this.costumerNr = costumerNr;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public void creditApplication(double sum, long time, String date) throws SQLException, IOException {
        Bank.creditWay(sum,this, time, date);
    }
}
