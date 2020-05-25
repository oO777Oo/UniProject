package bank.path;

public class Credit {
    int creditNr;
    double sum;
    Costumer costumer;
    long time;
    String date;
    long costumerNr;

    public Credit(double sum, Costumer costumer, long time, String date){
        this.sum = sum;
        this.costumer = costumer;
        this.time = time;
        this.date = date;
    }

    public Credit (int creditNr , double sum, long costumerNr, long time, String date){
        this.creditNr = creditNr;
        this.sum = sum;
        this.costumerNr = costumerNr;
        this.time = time;
        this.date = date;
    }

    public double getSum() {
        return sum;
    }

    public Costumer getCostumer() {
        return costumer;
    }

    public long getTime() {
        return time;
    }

    public String getDate() {
        return date;
    }

    public int getCreditNr() {
        return creditNr;
    }

    public void setCreditNr(int creditNr) {
        this.creditNr = creditNr;
    }

    public long getCostumerNr() {
        return costumerNr;
    }
}
