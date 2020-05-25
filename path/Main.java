package bank.path;


public class Main {
    public static void main(String[] args) throws ClassNotFoundException{
        try {
            Bank.dbInitialization();
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
        Bank.db.testConnection();
    }
}
