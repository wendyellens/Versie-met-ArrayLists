package sofa.internetbankieren.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import sofa.internetbankieren.model.Medewerker;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Wendy Ellens
 */
@SpringBootTest
class MedewerkerDAOTest {

    @Autowired MedewerkerDAO medewerkerDAO;
    @Autowired BedrijfDAO bedrijfDAO;

    @Test
    void medewerkerDAOtest() {
        Medewerker medewerker = new Medewerker("Voornaam", "Achternaam", bedrijfDAO);

        // test storeOne by checking whether personeelsnummer has been set by autoincrement
        medewerkerDAO.storeOne(medewerker);
        int generatedPersoneelsnr = medewerker.getPersoneelsnummer();
        assertNotEquals(0, generatedPersoneelsnr);

        // test getOneByID by checking whether there is an entry with the generated personeelsnummer
        assertNotNull(medewerkerDAO.getOneByID(generatedPersoneelsnr));

        // test getAll by checking whether the last in the list has the generated personeelsnummer
        List<Medewerker> medewerkers = medewerkerDAO.getAll();
        assertEquals(generatedPersoneelsnr, medewerkers.get(medewerkers.size() - 1).getPersoneelsnummer());

        // test updateOne by altering the rol of the newly stored entry
        medewerker.setRol(Medewerker.Rol.HOOFD_MKB);
        medewerkerDAO.updateOne(medewerker);
        assertEquals(Medewerker.Rol.HOOFD_MKB, medewerkerDAO.getOneByID(generatedPersoneelsnr).getRol());

//        // test deleteOne by checking whether the last entry does not have the generated personeelsnummer anymore
//        medewerkerDAO.deleteOne(medewerker);
//        medewerkers = medewerkerDAO.getAll();
//        assertNotEquals(generatedPersoneelsnr, medewerkers.get(medewerkers.size() - 1).getPersoneelsnummer());
    }
}
