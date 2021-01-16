package sofa.internetbankieren.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import sofa.internetbankieren.backing_bean.LoginFormBackingBean;
import sofa.internetbankieren.model.Bedrijf;
import sofa.internetbankieren.model.Klant;
import sofa.internetbankieren.model.Particulier;
import sofa.internetbankieren.repository.BedrijfDAO;
import sofa.internetbankieren.repository.ParticulierDAO;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
@Controller
public class LoginController {
    private final ParticulierDAO particulierDAO;
    private final BedrijfDAO bedrijfDAO;

    public LoginController(ParticulierDAO particulierDAO, BedrijfDAO bedrijfDAO) {
        this.particulierDAO = particulierDAO;
        this.bedrijfDAO = bedrijfDAO;
    }

    @GetMapping("/login")
    public String inlogHandler(Model model) {
      LoginFormBackingBean userDummy = new LoginFormBackingBean("", "");
      model.addAttribute("backingBean", userDummy);
      return "login";
    }

    @PostMapping("/overzicht")
    public String postInlogForm(Model model, @ModelAttribute LoginFormBackingBean dummy) {

        List<Particulier> particuliereklanten =
            particulierDAO.getOneByGebruikersnaamWachtwoord(dummy.getUserName(), dummy.getPassword());

        List<Bedrijf> bedrijfsklanten =
                bedrijfDAO.getOneByGebruikersnaamWachtwoord(dummy.getUserName(), dummy.getPassword());

        List<Klant> alleklanten = new ArrayList<>();
        alleklanten.addAll(particuliereklanten);
        alleklanten.addAll(bedrijfsklanten);

        if(alleklanten.size() == 0){ // Geen klant met deze inloggegevens
            return "foutingelogd";
        }
        else{
        model.addAttribute("ingelogde", alleklanten.get(0));
            return "overview";
        }
    }
}
