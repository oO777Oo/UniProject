package Client;

import java.io.Serializable;
import java.util.Date;

public class Credit implements Serializable {
    private int creditNr;
    private double sum;
    private long costumerNr;
    /* Date start and end will be calculated in Constructor */
    private Date start;
    private Date end;
    private double percentRange;
    /* answers will be initialized after answer from workers */
    private Short firstAns;
    private Short secondAns;
    private Short supervisorAns;


    public Credit() {
    }

    public Credit(double sum, long costumerNr) {
        this.sum = sum;
        this.costumerNr = costumerNr;
    }

    public Credit(int creditNr, double sum, long costumerNr) {
        this.creditNr = creditNr;
        this.sum = sum;
        this.costumerNr = costumerNr;
    }

    public Credit(int creditNr, double sum, long costumerNr, Date start, Date end) {
        this.creditNr = creditNr;
        this.sum = sum;
        this.costumerNr = costumerNr;
        this.start = start;
        this.end = end;
    }


    public Credit(int creditNr, double sum, long costumerNr, Date start, Date end, short firstAns, short secondAns, short supervisorAns) {
        this.creditNr = creditNr;
        this.sum = sum;
        this.costumerNr = costumerNr;
        this.start = start;
        this.end = end;
        this.firstAns = firstAns;
        this.secondAns = secondAns;
        this.supervisorAns = supervisorAns;
    }

    public int getCreditNr() {
        return creditNr;
    }

    public void setCreditNr(int creditNr) {
        this.creditNr = creditNr;
    }

    public double getSum() {
        return sum;
    }

    public void setSum(double sum) {
        this.sum = sum;
    }

    public long getCostumerNr() {
        return costumerNr;
    }

    public void setCostumerNr(long costumerNr) {
        this.costumerNr = costumerNr;
    }


    public Date getStart() {
        return start;
    }

    public void setStart(Date start) {
        this.start = start;
    }

    public Date getEnd() {
        return end;
    }

    public void setEnd(Date end) {
        this.end = end;
    }

    public Short getFirstAns() {
        return firstAns;
    }

    public void setFirstAns(Short firstAns) {
        this.firstAns = firstAns;
    }

    public Short getSecondAns() {
        return secondAns;
    }

    public void setSecondAns(Short secondAns) {
        this.secondAns = secondAns;
    }

    public Short getSupervisorAns() {
        return supervisorAns;
    }

    public void setSupervisorAns(Short supervisorAns) {
        this.supervisorAns = supervisorAns;
    }

    public boolean checkSuperCredit() {
        return !(this.sum < 500000.0);
    }

    public double getPercentRange() {
        return percentRange;
    }

    public void setPercentRange(double percentRange) {
        this.percentRange = percentRange;
    }
}
