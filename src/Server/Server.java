package Server;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.io.IOException;


public class Server {
    public static void main(String[] args) throws NamingException, IOException {
        /**
         * Server Main method need to be started when server is running
         * */
        System.out.println("Auto db initialization!");

        /* sql Manager is obj which can be remote to another JM  */
        SqlManager sqlManager = Initialization.sqlInitialization(); // Initialization is Class with static methods to
        // init DB for the program
        Initialization.dataBaseCreation(sqlManager); // Initialization of data base with meta data


        /* 2 rebind for context
         * first for queries with SqlManager() - port 1099
         * second for worker work with BankSystem() - port 1099
         */
        Context context = new InitialContext();
        context.rebind("rmi://localhost:1099/query", new SqlManager());
        context.rebind("rmi://localhost:1099/work", new BankSystem());
    }
}


