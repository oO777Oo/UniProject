package company;

import java.net.MalformedURLException;
import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.Registry;

public class Server {
    public static void main(String[] args) throws RemoteException, AlreadyBoundException, MalformedURLException, com.company.BadParameterException {


        // 1099 ist der “übliche” Port
        Registry reg = java.rmi.registry.LocateRegistry.createRegistry(1099);
        //PassagierImpl remoteObject = new PassagierImpl();
        //binding remote Object
        reg.bind("passagier", new com.company.PassagierImpl());


    }
}
