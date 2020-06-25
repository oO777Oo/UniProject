package Client;

import javax.naming.NamingException;
import java.rmi.RemoteException;
import java.util.Date;

import static Client.Bank.*;

public final class CeoWorker extends Worker implements Working {
    /**
     * Final class for ceoWorker.
     * Is final because i don't want another extends from this class
     */

    public CeoWorker() {
    }

    public CeoWorker(String firstName, String lastName, String birthday, String address,
                     long workerId, double salary, Date date) {
        super(firstName, lastName, birthday, address, workerId, salary, date);
    }

    @Override
    public void working() throws RemoteException, NamingException {
        workingMethod(this);
    }
}
