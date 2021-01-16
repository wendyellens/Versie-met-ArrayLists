package sofa.internetbankieren.controller;

/**
 * @Author Wichert Tjerkstra
 * aangemaakt op 9 dec
 *
 * Aangevuld door Wendy om de zakelijke registratie mogelijk te maken
 */

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import sofa.internetbankieren.backing_bean.LoginFormBackingBean;
import sofa.internetbankieren.backing_bean.RegisterFormPartBackingBean;
import sofa.internetbankieren.model.Bedrijf;
import sofa.internetbankieren.model.Bedrijfsrekening;
import sofa.internetbankieren.model.Klant;
import sofa.internetbankieren.model.Particulier;
import sofa.internetbankieren.repository.*;

@SessionAttributes({"klant", "particulier"})
@Controller
public class RegisterPageController {

    // Zoals aangegeven door de PO, is het hoofd MKB (medewerker 2) altijd de accountmanager.
    public final static int ID_ACCOUNTMANAGER = 2;

    ParticulierDAO particulierDAO;
    BedrijfDAO bedrijfDAO;
    MedewerkerDAO medewerkerDAO;
    BedrijfsrekeningDAO bedrijfsrekeningDAO;
    PriverekeningDAO priverekeningDAO;

    public RegisterPageController(ParticulierDAO particulierDAO, BedrijfDAO bedrijfDAO, MedewerkerDAO medewerkerDAO,
                                  BedrijfsrekeningDAO bedrijfsrekeningDAO, PriverekeningDAO priverekeningDAO) {
        super();
        this.particulierDAO = particulierDAO;
        this.bedrijfDAO = bedrijfDAO;
        this.medewerkerDAO = medewerkerDAO;
        this.bedrijfsrekeningDAO = bedrijfsrekeningDAO;
        this.priverekeningDAO = priverekeningDAO;
    }

    @GetMapping("/register")
    public String registerHandler() {
        return "register_page_1";
    }

    // keuze voor doorverwijzen naar zakelijke of particuliere registratiepagina
    @PostMapping("/register_Zakelijk_Particulier")
    public String choiceHandler(@RequestParam(name="zakelijkOfParticulier") int value, Model model){
        if (value == 0 ) {
            //model.addAttribute("klant", new Particulier());
            //return "register_page_2_particulier";
            model.addAttribute("backingBean", new RegisterFormPartBackingBean());
            return "register/particulier";
        }
        else if (value == 1) {
            model.addAttribute("klant", new Bedrijf());
            return "register_page_2_zakelijk";
        }
        return null;
    }

    // registratie particulier
    @PostMapping("/register_particulier")
    public String newParticulierHandler(Model model, @ModelAttribute(name="backingBean") RegisterFormPartBackingBean dummy) {
        model.addAttribute("backingBean", dummy);
        return "confirmationParticulier";
    }

    /*@PostMapping("/register_particulier")
    public String newParticulierHandler(
            @RequestParam(name="First_name") String voornaam,
            @RequestParam(name="Prefix", required = false) String voorvoegsels,
            @RequestParam(name="Last_name") String achternaam,
            @RequestParam(name="Birthday") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate geboortedatum,
            @RequestParam(name="BSN") int BSN,
            @RequestParam(name="Street") String straatnaam,
            @RequestParam(name="House_number") int huisnummer,
            @RequestParam(name="Postal_code") String postcode,
            @RequestParam(name="City") String woonplaats,
            Model model) {
        Particulier newParticulier = new Particulier(voornaam, voorvoegsels, achternaam,
                geboortedatum, BSN, straatnaam, huisnummer, postcode, woonplaats);
        model.addAttribute("klant", newParticulier);
        System.out.println(newParticulier);
        return "confirmationParticulier";
    }*/

    @PostMapping("/confirmParticulier")
    public String confirmHandler(@ModelAttribute RegisterFormPartBackingBean backingBean, Model model) {
        Particulier p = new Particulier(backingBean, bedrijfsrekeningDAO, priverekeningDAO);
        model.addAttribute("particulier", p);
        LoginFormBackingBean usernameForm = new LoginFormBackingBean("","");
        model.addAttribute("usernameForm", usernameForm);
        return "register/registerUsername";
    }

    @PostMapping("/usernameForm")
    public String confirm(Model model, @ModelAttribute LoginFormBackingBean usernameForm,
                          @ModelAttribute(name="particulier") Particulier p){
        Klant klant = (Klant) model.getAttribute("particulier");
        klant.setGebruikersnaam(usernameForm.getUserName());
        klant.setWachtwoord(usernameForm.getPassword());
        if (klant instanceof Particulier)
            particulierDAO.storeOne((Particulier) klant);
        else
            bedrijfDAO.storeOne((Bedrijf) klant);
        return "register_completed";
    }

/*    @PostMapping("/confirmParticulier")
    public String confirmHandler(@ModelAttribute(name="klant") Particulier confirmedMember, Model model)
    {
        model.addAttribute("klant", confirmedMember);
        return "register_page_3";
    }*/

    // registratie bedrijf
    @PostMapping("/register_zakelijk")
    public String newBedrijfsHandler(
            @RequestParam(name="Company_name") String bedrijfsnaam,
            @RequestParam(name="Branch") String sector,
            @RequestParam(name="KVK_number") int KVKNummer,
            @RequestParam(name="BTW_number") String BTWNummer,
            @RequestParam(name="Street") String straatnaam,
            @RequestParam(name="House_number") int huisnummer,
            @RequestParam(name="Postal_code") String postcode,
            @RequestParam(name="City") String woonplaats,
            Model model) {
        Bedrijf newBedrijf = new Bedrijf(straatnaam, huisnummer, postcode, woonplaats, bedrijfsnaam, KVKNummer, sector,
                BTWNummer, medewerkerDAO.getOneByID(ID_ACCOUNTMANAGER), bedrijfsrekeningDAO);
        model.addAttribute("klant", newBedrijf);
        return "confirmationBedrijf";
    }

    @PostMapping("/confirmBedrijf")
    public String confirmBedrijfHandler(@ModelAttribute(name="klant") Bedrijf confirmedMember, Model model) {
        model.addAttribute("klant", confirmedMember);
        return "register_page_3";
    }

    @PostMapping("/confirm")
    public String confirm(@RequestParam String user_name,
                          @RequestParam String password,
                          Model model){
        Klant klant = (Klant) model.getAttribute("klant");
        klant.setGebruikersnaam(user_name);
        klant.setWachtwoord(password);
        if (klant instanceof Particulier)
            particulierDAO.storeOne((Particulier) klant);
        else
            bedrijfDAO.storeOne((Bedrijf) klant);
        return "register_completed";
    }

}
