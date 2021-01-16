package sofa.internetbankieren.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import sofa.internetbankieren.backing_bean.LoginFormBackingBean;
import sofa.internetbankieren.model.Medewerker;
import sofa.internetbankieren.repository.MedewerkerDAO;

import java.util.List;

/**
 * @TacoJongkind 15-12-2020
 *
 * */
@Controller
public class MedewerkerLoginController {
    private MedewerkerDAO medewerkerDAO;

    public MedewerkerLoginController(MedewerkerDAO medewerkerDAO) {
        this.medewerkerDAO = medewerkerDAO;
    }

    @GetMapping("/login_medewerker")
    public String inlogMedewerkerHandler(Model model) {
        LoginFormBackingBean userDummy = new LoginFormBackingBean("", "");
        model.addAttribute("backingBean", userDummy);
        return "login_medewerker";
    }

    @PostMapping("/login_medewerker")
    public String postInlogForm(Model model, @ModelAttribute LoginFormBackingBean dummy) {
        System.out.println("inloggen");
        List<Medewerker> medewerkers =
                medewerkerDAO.getOneByGebruikersnaamWachtwoord(dummy.getUserName(), dummy.getPassword());
        if (medewerkers.size() == 0) { // Geen medewerker met deze inloggegevens
            System.out.println("onbestaande logingegevens");
            return "foutingelogd";
        } else {
            //model.addAttribute("ingelogde", medewerkers.get(0));
            System.out.println("ingelogd!");
            switch (medewerkers.get(0).getRol()) {
                case HOOFD_PARTICULIEREN:
                    return "overviewHoofdParticulierenDummy";

                case HOOFD_MKB:
                    return "overviewHoofdMkbDummy";

                case ACCOUNTMANAGER:
                    return "overviewAccountmanagerDummy";

                default:
                    return "error";
            }
        }
    }



}
