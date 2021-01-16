package sofa.internetbankieren.model;

import sofa.internetbankieren.repository.TransactieDAO;

import java.util.List;

/**
 * @author WichertTjerkstra 7 dec aangemaakt
 * review: Wendy Ellens, 8 dec
 */

public class Bedrijfsrekening extends Rekening {

    private Particulier contactpersoon;
    private Bedrijf rekeninghouder;

    public Bedrijfsrekening() {
        super();
    }

    public Bedrijfsrekening(int idRekening, String IBAN, double saldo, List<Integer> transactieIDs,
                            TransactieDAO transactieDAO, Particulier contactpersoon, Bedrijf rekeninghouder) {
        super(idRekening, IBAN, saldo, transactieIDs, transactieDAO);
        this.contactpersoon = contactpersoon;
        this.rekeninghouder = rekeninghouder;
    }

    public Particulier getContactpersoon() {
        return contactpersoon;
    }

    // toegevoegd door Wendy
    @Override
    public String getTenaamstelling() {
        return rekeninghouder.getBedrijfsnaam();
    }

    public void setContactpersoon(Particulier contactpersoon) {
        this.contactpersoon = contactpersoon;
    }

    public Bedrijf getRekeninghouder() {
        return rekeninghouder;
    }

    public void setRekeninghouder(Bedrijf rekeninghouder) {
        this.rekeninghouder = rekeninghouder;
    }

    // toegevoegd door Wendy
    @Override
    public List<Transactie> getTransacties() {
        return super.getTransactieDAO().getAllByIDBedrijfsrekening(super.getIdRekening());
    }

    // TODO 7/12 wat hebben we nodig in de toString?
    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append(super.toString());
        result.append(" Rekeninghouder: " + rekeninghouder);
        result.append(" Contactpersoon: " + contactpersoon);
        return result.toString();
    }
}
