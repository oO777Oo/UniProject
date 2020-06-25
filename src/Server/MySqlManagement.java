package Server;

import java.io.Serializable;


public class MySqlManagement extends DataBase implements Serializable {
    /**
     * MySQL class for connection to MySQL DB
     */
    public MySqlManagement(String name, String pass, String url, String init) {
        super(name, pass, url, init);
    }

    public MySqlManagement(String name, String pass, String url) {
        super(name, pass, url);
    }

    public MySqlManagement() {
    }

}
