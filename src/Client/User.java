package Client;

import Server.QueriesImp;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.rmi.RemoteException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static Client.Bank.*;

public class User extends Human {
    /**
     * User class for Bank costumers
     */
    private long costumerNr;

    public User() {
    }

    public User(String firstName, String lastName, String birthday, String address, long costumerNr) {
        super(firstName, lastName, birthday, address);
        this.costumerNr = costumerNr;
    }

    public long getCostumerNr() {
        return costumerNr;
    }

    public void setCostumerNr(long costumerNr) {
        this.costumerNr = costumerNr;
    }

    public void creditApplication(double sum, String date) throws NamingException, RemoteException {
        Credit credit = new Credit(sum, this.costumerNr);
        String pattern = "dd-MM-yyyy";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        try {
            Date expireDate = simpleDateFormat.parse(date);

            credit.setEnd(expireDate);
            credit.setStart(new Date());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Context context = new InitialContext();
        QueriesImp queriesImp = (QueriesImp) context.lookup("rmi://localhost/query");
        Account userAccount = queriesImp.selectAccountQuery(credit);
        if (userAccount.getCreditRating() < credit.getSum() || credit.getSum() >= 500000) {
            getCreditApplication(credit, false);
        } else {
            userAccount.setCreditStatus(answer(credit).toString());
            getCreditApplication(credit, true);
            queriesImp.updateStatus(userAccount);
        }
    }

}

