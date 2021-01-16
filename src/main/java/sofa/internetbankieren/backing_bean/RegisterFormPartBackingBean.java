package sofa.internetbankieren.backing_bean;

import java.time.LocalDate;

public class RegisterFormPartBackingBean {

    private String voornaam;
    private String tussenvoegsels;
    private String achternaam;
    private String geboortedatum;  // Let op dit moet yyyy-MM-dd worden!
    private int BSN;
    private String straat;
    private int huisnummer;
    private String postcode;
    private String woonplaats;

    public RegisterFormPartBackingBean() {
    }

    public RegisterFormPartBackingBean(String voornaam, String tussenvoegsels, String achternaam, String geboortedatum,
                                       int BSN, String straat, int huisnummer, String postcode, String woonplaats) {
        this.voornaam = voornaam;
        this.tussenvoegsels = tussenvoegsels;
        this.achternaam = achternaam;
        this.geboortedatum = geboortedatum;
        this.BSN = BSN;
        this.straat = straat;
        this.huisnummer = huisnummer;
        this.postcode = postcode;
        this.woonplaats = woonplaats;
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

    public String getGeboortedatum() {
        return geboortedatum;
    }

    public void setGeboortedatum(String geboortedatum) {
        this.geboortedatum = geboortedatum;
    }

    public int getBSN() {
        return BSN;
    }

    public void setBSN(int BSN) {
        this.BSN = BSN;
    }

    public String getStraat() {
        return straat;
    }

    public void setStraat(String straat) {
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
}
