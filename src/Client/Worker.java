package Client;

import java.util.Date;

public class Worker extends Human {
    /**
     * Superclass for all Workers
     * class is with default access
     * because calling his methods will be able only through inheritance
     */
    long workerId;
    double salary;
    Date startWorkDate;

    public Worker() {
    }

    public Worker(String firstName, String lastName, String birthday, String address,
                  long workerId, double salary, Date date) {
        super(firstName, lastName, birthday, address);
        this.workerId = workerId;
        this.salary = salary;
        this.startWorkDate = date;
    }
}
