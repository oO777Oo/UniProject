package Client;

import javax.naming.NamingException;
import java.rmi.RemoteException;
import java.util.Date;

import static Client.Bank.*;

public final class CreditWorker extends Worker implements Working {
    /**
     * Final class for credit workers
     * Is final because i don't want another extends from this class
     */
    public CreditWorker() {
    }

    public CreditWorker(String firstName, String lastName, String birthday, String address,
                        long workerId, double salary, Date date) {
        super(firstName, lastName, birthday, address, workerId, salary, date);
    }

    @Override
    public void working() throws RemoteException, NamingException {
        /** Implementation of working Interface*/
        workingMethod(this);
    }
}
