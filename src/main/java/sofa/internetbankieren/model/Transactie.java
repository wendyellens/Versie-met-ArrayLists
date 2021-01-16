package sofa.internetbankieren.model;

import java.time.LocalDateTime;

/**
 * @author Wendy Ellens
 *
 * Modelleert een banktransactie (een bij- of afschrijving)
 */
public class Transactie implements Comparable<Transactie> {

    private int idTransactie;
    private Rekening rekening;
    private boolean bijschrijving; // false voor afschrijvingen
    private double bedrag;
    private LocalDateTime datum;
    private String omschrijving;
    private Rekening tegenRekening;

    public Transactie() {
        super();
    }

    public Transactie(int idTransactie, Rekening verzender, double bedrag, LocalDateTime datum, String omschrijving,
                      Rekening ontvanger) {
        super();
        this.idTransactie = idTransactie;
        this.rekening = verzender;
        this.bijschrijving = false; // Alleen afschrijvingen mogelijk. Bijschrijving wordt gemaakt door DAO.
        this.bedrag = bedrag;
        this.datum = datum;
        this.omschrijving = omschrijving;
        this.tegenRekening = ontvanger;
    }

    public Transactie(Rekening verzender, double bedrag, LocalDateTime datum, String omschrijving, Rekening ontvanger) {
        this(0, verzender, bedrag, datum, omschrijving, ontvanger);
    }

    // Kloon maken
    public Transactie(Transactie andereTransactie) {
        super();
        this.idTransactie = andereTransactie.idTransactie;
        this.rekening = andereTransactie.rekening;
        this.bijschrijving = andereTransactie.bijschrijving;
        this.bedrag = andereTransactie.bedrag;
        this.datum = andereTransactie.datum;
        this.omschrijving = andereTransactie.omschrijving;
        this.tegenRekening = andereTransactie.tegenRekening;
    }

    public int getIdTransactie() {
        return idTransactie;
    }

    public void setIdTransactie(int idTransactie) {
        this.idTransactie = idTransactie;
    }

    public Rekening getRekening() {
        return rekening;
    }

    public void setRekening(Rekening rekening) {
        this.rekening = rekening;
    }

    public boolean isBijschrijving() {
        return bijschrijving;
    }

    public void setBijschrijving(boolean bijschrijving) {
        this.bijschrijving = bijschrijving;
    }

    public double getBedrag() {
        return bedrag;
    }

    public void setBedrag(double bedrag) {
        this.bedrag = bedrag;
    }

    public LocalDateTime getDatum() {
        return datum;
    }

    public void setDatum(LocalDateTime datum) {
        this.datum = datum;
    }

    public String getOmschrijving() {
        return omschrijving;
    }

    public void setOmschrijving(String omschrijving) {
        this.omschrijving = omschrijving;
    }

    public Rekening getTegenRekening() {
        return tegenRekening;
    }

    public void setTegenRekening(Rekening tegenRekening) {
        this.tegenRekening = tegenRekening;
    }

    @Override
    public int compareTo(Transactie other) {
        return this.datum.compareTo(other.datum);
    }
}
