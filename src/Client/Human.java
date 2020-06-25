package Client;

import java.io.Serializable;

public class Human implements Serializable {
    /**
     * Super class for all Humans like User, Worker
     * class is with default access
     * because calling his methods will be able only through inheritance
     */
    private String firstName;
    private String lastName;
    private String birthday;
    private String address;

    public Human() {
    }

    public Human(String firstName, String lastName, String birthday, String address) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthday = birthday;
        this.address = address;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}

