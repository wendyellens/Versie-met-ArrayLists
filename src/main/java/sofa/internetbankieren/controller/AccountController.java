package sofa.internetbankieren.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import sofa.internetbankieren.model.Rekening;
import sofa.internetbankieren.model.Transactie;
import sofa.internetbankieren.repository.BedrijfsrekeningDAO;
import sofa.internetbankieren.repository.PriverekeningDAO;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Wendy Ellens
 */
@Controller
public class AccountController {
    private static final int MAX_TRANSACTIES = 10;
    private final PriverekeningDAO priverekeningDAO;
    private final BedrijfsrekeningDAO bedrijfsrekeningDAO;

    public AccountController(PriverekeningDAO priverekeningDAO, BedrijfsrekeningDAO bedrijfsrekeningDAO) {
        super();
        this.priverekeningDAO = priverekeningDAO;
        this.bedrijfsrekeningDAO = bedrijfsrekeningDAO;
    }

    @GetMapping("/rekening/{IBAN}")
    public String accountHandler(Model model, @PathVariable("IBAN") String iban) {
        List<Rekening> rekeningen = new ArrayList<>();
        rekeningen.addAll(priverekeningDAO.getOneByIban(iban));
        rekeningen.addAll(bedrijfsrekeningDAO.getOneByIban(iban));
        Rekening rekening = rekeningen.get(0);
        List<Transactie> transacties = rekening.getTransacties();
        Collections.reverse(transacties);
        if (transacties.size() > 0)
            transacties = transacties.subList(0, Math.min(transacties.size(), MAX_TRANSACTIES));
        model.addAttribute("rekening", rekening);
        model.addAttribute("transacties", transacties);
        model.addAttribute("nu", LocalDateTime.now());
        return "account";
    }
}
