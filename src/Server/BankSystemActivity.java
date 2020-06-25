package Server;

import Client.Account;
import Client.Credit;
import Client.SuperCredit;
import Client.Worker;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Stack;

public interface BankSystemActivity extends Remote {
    Stack<Credit> doWork(Worker worker) throws RemoteException;

    void setAnswerInDb(Credit credit, boolean flag) throws RemoteException;

    void setAnswerInDbSupervisor(Credit credit) throws RemoteException;

    void setAnswerInDbCeo(SuperCredit credit) throws RemoteException;

    void updateCreditSum(Account account, Credit credit) throws RemoteException;
}
