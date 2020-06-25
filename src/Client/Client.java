package Client;

import javax.naming.NamingException;
import java.rmi.RemoteException;


public class Client {
    public static void main(String[] args) throws NamingException, RemoteException, InterruptedException {
        /* Create 3 obj User */
        User user1 = new User("Anton", "Müller", "13.06.1982", "München", 1);
        User user2 = new User("Chriss", "Locov", "09.03.1990", "Köln", 2);
        User user3 = new User("Kevin", "Miron", "02.01.1988", "Stuttgart", 3);

        /* Create 4 worker -> 2 creditWorkers, 1 supervisorWorker and ceoWorker */
        CreditWorker creditWorker = new CreditWorker();
        CreditWorker creditWorker1 = new CreditWorker();
        SupervisorWorker supervisorWorker = new SupervisorWorker();
        CeoWorker ceoWorker = new CeoWorker();

        /* User apply for credit */
        user1.creditApplication(1000, "25-09-2022");
        user2.creditApplication(600000, "28-10-2055");
        user3.creditApplication(25000, "15-05-2025");

        /* Thread sleep is used only to check what is the output
         * First worker works
         * */
        Thread.sleep(5000);
        System.out.println("Worker Theo starts to control the credit requests from db");
        creditWorker.working();
        Thread.sleep(5000);
        System.out.println();

        /* Thread sleep is used only to check what is the output
         * Second worker works
         * */
        System.out.println("Worker Samuil starts to control the credit requests from db");
        creditWorker1.working();
        Thread.sleep(5000);
        System.out.println();

        /* Thread sleep is used only to check what is the output
         * Supervisor worker works
         * */
        System.out.println("Supervisor starts to control the credit requests from db");
        System.out.println();
        supervisorWorker.working();
        Thread.sleep(5000);
        System.out.println();

        /* Thread sleep is used only to check what is the output
         * Ceo worker works
         * */
        System.out.println("Ceo starts to control the credit requests from db");
        System.out.println();
        ceoWorker.working();
        Thread.sleep(10000);
    }
}
