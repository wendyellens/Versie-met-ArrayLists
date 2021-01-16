package sofa.internetbankieren.model;

import sofa.internetbankieren.repository.BedrijfDAO;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Wendy Ellens
 *
 * Modelleert een bankmedewerker.
 * Reviewed by Wichert 11-12
 */
public class Medewerker {

    public enum Rol {
        HOOFD_PARTICULIEREN,
        HOOFD_MKB,
        ACCOUNTMANAGER
    }

    private int personeelsnummer;
    private String gebruikersnaam;
    private String wachtwoord;
    private String voornaam;
    private String tussenvoegsels;
    private String achternaam;
    private Rol rol;
    private List<Integer> bedrijfIDs; // Bedrijven waarvoor de medewerker accountmanager is
    private final BedrijfDAO bedrijfDAO;

    public Medewerker(int personeelsnummer, String gebruikersnaam, String wachtwoord, String voornaam,
                      String tussenvoegsels, String achternaam, Rol rol, List<Integer> bedrijfIDs, BedrijfDAO bedrijfDAO) {
        super();
        this.personeelsnummer = personeelsnummer;
        this.gebruikersnaam = gebruikersnaam;
        this.wachtwoord = wachtwoord;
        this.voornaam = voornaam;
        this.tussenvoegsels = tussenvoegsels;
        this.achternaam = achternaam;
        this.rol = rol;
        this.bedrijfIDs = bedrijfIDs;
        this.bedrijfDAO = bedrijfDAO;
    }

    public Medewerker(String voornaam, String achternaam, BedrijfDAO bedrijfDAO) {
        this(0, "", "", voornaam, "", achternaam, Rol.ACCOUNTMANAGER, new ArrayList<>(), bedrijfDAO);
    }

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

    public int getPersoneelsnummer() {
        return personeelsnummer;
    }
    public void setPersoneelsnummer(int personeelsnummer) {
        this.personeelsnummer = personeelsnummer;
    }


    public String getVoornaam() {
        return voornaam;
    }
    public void setVoornaam(String voornaam) {
        this.voornaam = voornaam;
    }

    public String getTussenvoegsels() {
        return tussenvoegsels;
    }
    public void setTussenvoegsels(String tussenvoegsels) {
        this.tussenvoegsels = tussenvoegsels;
    }

    public String getAchternaam() {
        return achternaam;
    }
    public void setAchternaam(String achternaam) {
        this.achternaam = achternaam;
    }

    public Rol getRol() {
        return rol;
    }
    public void setRol(Rol rol) {
        this.rol = rol;
    }

    public List<Bedrijf> getBedrijven() {
        return bedrijfDAO.getAllByIdAccountmanager(personeelsnummer);
    }
    public void setBedrijfIDs(List<Integer> bedrijfIDs) {
        this.bedrijfIDs = bedrijfIDs;
    }

    @Override
    public String toString() {
        return "Medewerker{" +
                "personeelsnummer=" + personeelsnummer +
                ", gebruikersnaam='" + gebruikersnaam + '\'' +
                ", wachtwoord='" + wachtwoord + '\'' +
                ", voornaam='" + voornaam + '\'' +
                ", tussenvoegsels='" + tussenvoegsels + '\'' +
                ", achternaam='" + achternaam + '\'' +
                ", rol=" + rol +
                ", bedrijven=" + bedrijfIDs +
                '}';
    }
}
