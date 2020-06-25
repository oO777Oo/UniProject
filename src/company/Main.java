package company;

import java.net.MalformedURLException;
import java.rmi.*;
import java.sql.*;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws SQLException, com.company.BadParameterException, InputMismatchException, UserFriendlyException, RemoteException, NotBoundException, MalformedURLException, com.company.UserFriendlyException {
        /* URL der Datenbank (Server-Adresse und ggf. Datenbank-Name) */
        String dbURL = "jdbc:mariadb://localhost:3306/appair";
        /* Driver (hier für mariaDB) */
        String driver = "org.mariadb.jdbc.Driver";
        /* Verbindung zur Datenbank */
        Connection con = null;
        try{
            Class.forName(driver);
            con = DriverManager.getConnection(dbURL, "root", "");

        }
        catch (SQLException | ClassNotFoundException e){
            e.printStackTrace();
        }

        Scanner sc = new Scanner(System.in); // neues Scanner-Objekt sc

            //lockup und casting
        com.company.Passagier p = (com.company.Passagier) Naming.lookup("passagier");
            System.out.println("Geben Sie bitte Ihre Passagiernummer an:");
            int epnr = sc.nextInt(); // lokale Variable speichert die Eingabe
            p.setPnr(epnr);
        
        com.company.Flug flug = new com.company.Flug();
        try {
            System.out.println("Geben Sie bitte Ihre Flugnummer an:");
            int eflugnr = sc.nextInt(); // lokale Variable speichert die Eingabe
            flug.setFlugnr(eflugnr);
        }catch(InputMismatchException  e){
            //e.printStackTrace();
            throw new com.company.BadParameterException("Geben Sie bitte keine Buchstaben ein.");
        }

        com.company.Antrag antrag = new com.company.Antrag(p.getPnr(), flug.getFlugnr());

        String sql = "SELECT Distanz, Verspaetung FROM antrag WHERE Passagiernr =" + antrag.getPnr() + " AND Flugnr = " + antrag.getFlugnr();
        Statement stat = con.createStatement();
        ResultSet res = stat.executeQuery(sql);
        try
        {
            res.first();
            if(res.getInt("Distanz")!=0 && res.getInt("Verspaetung")!=0) {
                    // jeweils Spalten mit Namen 'Distanz' und 'Verspaetung' ausgeben
                    antrag.setDistanz(res.getInt("Distanz"));
                    antrag.setVerspaetung(res.getInt("Verspaetung"));
                    System.out.println(antrag.getDistanz());
                    System.out.println(antrag.getVerspaetung());
            }
        } catch(SQLDataException e){
           throw new com.company.UserFriendlyException("Die Passagiernummer oder die Flugnummer existiert nicht. Bitte überprüfen Sie Ihre Eingaben.");
           // e.printStackTrace();
        }
        if(antrag.verspaetungPruefen() == true){
            antrag.angebotErtstellen();
        }
        String antwort = sc.next();
        if(antwort.equals("+")){
            System.out.println("Der Betrag wird auf Ihr Konto überwiesen.");
        } else{
            com.company.Experte experte = new com.company.Experte(antwort);
        }
    }
}
