package sofa.internetbankieren.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import sofa.internetbankieren.model.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Wendy Ellens
 */
@SpringBootTest
class TransactieDAOTest {

    @Autowired MedewerkerDAO medewerkerDAO;
    @Autowired BedrijfDAO bedrijfDAO;
    @Autowired ParticulierDAO particulierDAO;
    @Autowired BedrijfsrekeningDAO bedrijfsrekeningDAO;
    @Autowired PriverekeningDAO priverekeningDAO;
    @Autowired TransactieDAO transactieDAO;

    // Model test objects to store in the database
    Medewerker medewerker;
    Bedrijf bedrijf;
    Particulier particulier;
    Bedrijfsrekening bedrijfsrekening;
    Priverekening priverekening;
    Transactie overboeking_particulier_bedrijf;

    @Test
    void transactieDAOtest() {
        setDBEntries();
        storeDbEntries();

        // test storeOne by checking whether IdTransactie has been set by autoincrement
        transactieDAO.storeOne(overboeking_particulier_bedrijf);
        int generatedIdPrivetransactie = overboeking_particulier_bedrijf.getIdTransactie();
        int generatedIdBedrijfstransactie = generatedIdPrivetransactie + 1;
        System.out.println("IDTransactie: " + generatedIdPrivetransactie);
        assertNotEquals(0, generatedIdPrivetransactie);

        // test getOneByID by checking whether there is an entry with the generated ID
        assertNotNull(transactieDAO.getOneByID(generatedIdPrivetransactie));

        // test getAll by checking whether the last in the list has the generated ID
        List<Transactie> transacties = transactieDAO.getAll();
        assertEquals(generatedIdBedrijfstransactie, transacties.get(transacties.size() - 1).getIdTransactie());

        // test getAllByIDPriverekening by checking whether the last in the list has the generated IdPrivetransactie
        List<Transactie> privetransacties
                = transactieDAO.getAllByIDPriverekening(priverekening.getIdRekening());
        assertEquals(generatedIdPrivetransactie,
                privetransacties.get(privetransacties.size() - 1).getIdTransactie());

        // test getAllByIDBedrijfsrekening by checking whether the last in the list has the generated ID
        List<Transactie> bedrijfstransacties
                = transactieDAO.getAllByIDBedrijfsrekening(bedrijfsrekening.getIdRekening());
        assertEquals(generatedIdBedrijfstransactie,
                bedrijfstransacties.get(bedrijfstransacties.size() - 1).getIdTransactie());

        // test updateOne by altering the omschrijving of one of the stored entries
        overboeking_particulier_bedrijf.setOmschrijving("Bijschrijving");
        transactieDAO.updateOne(overboeking_particulier_bedrijf);
        assertEquals("Bijschrijving", transactieDAO.getOneByID(generatedIdPrivetransactie).getOmschrijving());

        // test deleteOne by checking whether the generated ID is not present anymore
        transactieDAO.deleteOne(overboeking_particulier_bedrijf);
        transacties = transactieDAO.getAll();
        assertNotEquals(generatedIdPrivetransactie, transacties.get(transacties.size() - 2).getIdTransactie());

        deleteDbEntries();
    }

    private void setDBEntries() {
        medewerker = new Medewerker("Voornaam", "Achternaam", bedrijfDAO);
        bedrijf = new Bedrijf(0, "", "", "", 0, "",
                "", "", 0, "", "", medewerker, new ArrayList<>(),
                bedrijfsrekeningDAO);
        particulier = new Particulier(0, "", "", "", 0,
                "", "", "", "", "", LocalDate.now(), 1,
                new ArrayList<>(), new ArrayList<>(), bedrijfsrekeningDAO, priverekeningDAO);
        bedrijfsrekening = new Bedrijfsrekening(0, "10", 0, new ArrayList<>(), transactieDAO,
                particulier, bedrijf);
        priverekening = new Priverekening(0, "11", 0, new ArrayList<>(), transactieDAO,
                particulier);
        overboeking_particulier_bedrijf = new Transactie(priverekening, 1.5, LocalDateTime.now(),
                "", bedrijfsrekening);
    }

    private void storeDbEntries() {
        medewerkerDAO.storeOne(medewerker);
        System.out.println("Personeelsnummer" + medewerker.getPersoneelsnummer());
        bedrijfDAO.storeOne(bedrijf);
        System.out.println("IdBedrijf" + bedrijf.getIdKlant());
        particulierDAO.storeOne(particulier);
        System.out.println("IdParticulier" + particulier.getIdKlant());
        bedrijfsrekeningDAO.storeOne(bedrijfsrekening);
        System.out.println("IdBedrijfsekening" + bedrijfsrekening.getIdRekening());
        priverekeningDAO.storeOne(priverekening);
        System.out.println("IdPriverekening" + priverekening.getIdRekening());
    }

    private void deleteDbEntries() {
        bedrijfsrekeningDAO.deleteOne(bedrijfsrekening);
        priverekeningDAO.deleteOne(priverekening);
        bedrijfDAO.deleteOne(bedrijf);
        particulierDAO.deleteOne(particulier);
        medewerkerDAO.deleteOne(medewerker);
    }
}