package Client;

public class SuperCredit extends Credit {
    private Short ceoAns;

    public SuperCredit(Credit credit) {
        super(credit.getCreditNr(), credit.getSum(), credit.getCostumerNr(), credit.getStart(), credit.getEnd());
    }

    public SuperCredit(Credit credit, Short ceoAns) {
        super(credit.getCreditNr(), credit.getSum(), credit.getCostumerNr(), credit.getStart(), credit.getEnd(),
                credit.getFirstAns(), credit.getSecondAns(), credit.getSupervisorAns());
        this.ceoAns = ceoAns;
    }

    public Short getCeoAns() {
        return ceoAns;
    }

    public void setCeoAns(Short ceoAns) {
        this.ceoAns = ceoAns;
    }

}

