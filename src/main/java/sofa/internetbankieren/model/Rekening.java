package sofa.internetbankieren.model;

/**
 * @WicherTjerkstra 7 dec aangemaakt
 * review: Wendy Ellens, 8 dec
 */

import sofa.internetbankieren.repository.TransactieDAO;

import java.util.List;

public abstract class Rekening {

    private int idRekening;
    private String IBAN;
    private double saldo;
    private List<Integer> transactieIDs;
    private TransactieDAO transactieDAO;

    public Rekening() { super(); }

    public Rekening(int idRekening, String IBAN, double saldo, List<Integer> transactieIDs,
                    TransactieDAO transactieDAO) {
        this.idRekening = idRekening;
        this.IBAN = IBAN;
        this.saldo = saldo;
        this.transactieIDs = transactieIDs;
        this.transactieDAO = transactieDAO;
    }

    // toegevoegd door Wendy
    public abstract String getTenaamstelling();

    public int getIdRekening() {
        return idRekening;
    }

    public void setIdRekening(int idRekening) {
        this.idRekening = idRekening;
    }

    public String getIBAN() {
        return IBAN;
    }

    public void setIBAN(String IBAN) {
        this.IBAN = IBAN;
    }

    public double getSaldo() {
        return saldo;
    }

    public void setSaldo(double saldo) {
        this.saldo = saldo;
    }

    // toegevoegd door Wendy
    public abstract List<Transactie> getTransacties();

    public void setTransactieIDs(List<Integer> transactieIDs) {
        this.transactieIDs = transactieIDs;
    }

    public TransactieDAO getTransactieDAO() {
        return transactieDAO;
    }

    public void setTransactieDAO(TransactieDAO transactieDAO) {
        this.transactieDAO = transactieDAO;
    }

    // TODO 7/12 wat hebben we nodig in de toString?
    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append("Rekening: " + IBAN);
        result.append(", Huidige saldo: " + saldo);
        return result.toString();
    }
}
