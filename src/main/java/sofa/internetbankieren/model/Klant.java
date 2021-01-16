package sofa.internetbankieren.model;

import sofa.internetbankieren.repository.BedrijfsrekeningDAO;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Taco Jongkind & Hemza Lasri, 07-12-2020
 *
 * */

public abstract class Klant {

    private int idKlant;
    private String gebruikersnaam;
    private String wachtwoord;
    private String straat;
    private int huisnummer;
    private String postcode;
    private String woonplaats;
    private List<Integer> rekeningIDs = new ArrayList<>(); // verplaatst vanuit subklassen door Wendy
    private BedrijfsrekeningDAO bedrijfsrekeningDAO;

    public Klant(int idKlant, String gebruikersnaam, String wachtwoord, String straat, int huisnummer, String postcode,
                 String woonplaats, List<Integer> rekeningIDs, BedrijfsrekeningDAO bedrijfsrekeningDAO) {
        this.idKlant = idKlant;
        this.gebruikersnaam = gebruikersnaam;
        this.wachtwoord = wachtwoord;
        this.straat = straat;
        this.huisnummer = huisnummer;
        this.postcode = postcode;
        this.woonplaats = woonplaats;
        this.rekeningIDs = rekeningIDs;
        this.bedrijfsrekeningDAO = bedrijfsrekeningDAO;
    }

    public Klant() {
    }

    // toegevoegd door Wendy
    public abstract String getNaam();

    public String getGebruikersnaam() {
        return gebruikersnaam;
    }

    public void setGebruikersnaam(String gebruikersnaam) {
        this.gebruikersnaam = gebruikersnaam;
    }

    public String getWachtwoord() {
        return wachtwoord;
    }

    public void setWachtwoord(String wachtwoord) {
        this.wachtwoord = wachtwoord;
    }

    public int getIdKlant() {
        return idKlant;
    }

    public void setIdKlant(int idKlant) {
        this.idKlant = idKlant;
    }

    public String getStraat() {
        return straat;
    }

    public void setStraat (String straat) {
        this.straat = straat;
    }

    public int getHuisnummer() {
        return huisnummer;
    }

    public void setHuisnummer(int huisnummer) {
        this.huisnummer = huisnummer;
    }

    public String getPostcode() {
        return postcode;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }

    public String getWoonplaats() {
        return woonplaats;
    }

    public void setWoonplaats(String woonplaats) {
        this.woonplaats = woonplaats;
    }

    // toegevoegd door Wendy
    public abstract List<? extends Rekening> getRekeningen();

    // toegevoegd door Wendy
    public void setRekeningIDs(List<Integer> rekeningIDs) {
        this.rekeningIDs = rekeningIDs;
    }

    public BedrijfsrekeningDAO getBedrijfsrekeningDAO() {
        return bedrijfsrekeningDAO;
    }

    public void setBedrijfsrekeningDAO(BedrijfsrekeningDAO bedrijfsrekeningDAO) {
        this.bedrijfsrekeningDAO = bedrijfsrekeningDAO;
    }

    @Override
    public String toString() {
        return "Klant{" +
                "idKlant=" + idKlant +
                ", gebruikersnaam='" + gebruikersnaam + '\'' +
                ", wachtwoord='" + wachtwoord + '\'' +
                ", straat='" + straat + '\'' +
                ", huisnummer=" + huisnummer +
                ", postcode='" + postcode + '\'' +
                ", woonplaats='" + woonplaats + '\'' +
                ", rekeningIDs=" + rekeningIDs +
                '}';
    }
}
