package Server;

import Client.Account;
import Client.Credit;
import Client.DbData;
import Client.User;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.sql.Connection;
import java.util.Date;

public interface QueriesImp extends Remote {
    Account selectAccountQuery(Credit credit) throws RemoteException;

    String insertStringBuilder(String table) throws RemoteException;

    User selectUserQuery(Credit credit) throws RemoteException;

    void insertCreditQuery(int creditNr, double sum, Long costumerNr, Date start, Date end) throws RemoteException;

    void updateStatus(Account account) throws RemoteException;

    void autoCreditApproved(Credit credit) throws RemoteException;

    MySqlManagement getMySql() throws RemoteException;

    void setMySqlParamName(DbData data) throws RemoteException;

    Connection getConnection() throws RemoteException;
}

