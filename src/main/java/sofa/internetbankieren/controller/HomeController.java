package sofa.internetbankieren.controller;

/**
 * @author Wichert Tjerkstra 7 dec aangemaakt
 *
 */

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    public HomeController() {
        super();
    }

    @GetMapping({"/", "/home"})
    public String startHandler() {
        return "home";
    }
}