package bank.path;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;


public interface CreditDecision {
    static void decideCredit(Credit credit) throws SQLException, IOException {
        Connection connection = Bank.db.getRes();
        creditRates rating = Bank.creditRate(credit, connection);
        if (rating.equals(creditRates.VeryGood) || rating.equals(creditRates.Good)) {
            System.out.println("Credit approved!");
            Bank.approvedCredit(credit);
        } else {
            Bank.setNewCredit(credit);
        }
    }
    void workerDecide(Credit credit) throws SQLException, IOException;
}
