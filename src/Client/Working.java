package Client;

import javax.naming.NamingException;
import java.rmi.RemoteException;

public interface Working {
    void working() throws RemoteException, NamingException;
}
