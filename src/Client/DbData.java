package Client;

import java.io.Serializable;

public class DbData implements Serializable {
    /* Data structure to save console input for db connection */
    String name;
    String pass;
    String url;
    String initUrl;

    public DbData(String name, String pass, String url, String initUrl) {
        this.name = name;
        this.pass = pass;
        this.url = url;
        this.initUrl = initUrl;
    }

    public String getName() {
        return name;
    }

    public String getPass() {
        return pass;
    }

    public String getUrl() {
        return url;
    }

    public String getInitUrl() {
        return initUrl;
    }
}
