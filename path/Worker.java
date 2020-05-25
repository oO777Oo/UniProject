package bank.path;

public class Worker extends Human {
    long workerNr;
    String status;
    double salary;
    String date;
    boolean sex;

    public Worker(String firstName, String lastName, String birthday, String address, String status, Boolean sex, double salary, String date){
        super(firstName, lastName, birthday, address);
        this.status = status;
        this.date = date;
        this.sex = sex;
        this.salary = salary;

    }

    public void setWorkerNr(int workerNr) {
        this.workerNr = workerNr;
    }

    public long getWorkerNr() {
        return this.workerNr;
    }


}
