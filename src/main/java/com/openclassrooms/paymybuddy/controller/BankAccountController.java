package com.openclassrooms.paymybuddy.controller;

import com.openclassrooms.paymybuddy.model.BankAccount;
import com.openclassrooms.paymybuddy.utils.Constante;
import com.openclassrooms.paymybuddy.model.User;
import com.openclassrooms.paymybuddy.service.BankAccountService;
import com.openclassrooms.paymybuddy.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;


@Controller
@RequestMapping("bankAccount")
public class BankAccountController {

    @Autowired
    private UserService userService ;

    @Autowired
    private BankAccountService bankAccountService;

    @PostMapping
    public ModelAndView addBankAccount(@ModelAttribute("bankAccount") final BankAccount bankAccount, Authentication auth) {

        User user = new User();
        user = userService.findUserByEmail(auth.getName());
        ModelAndView modelAndView = new ModelAndView("redirect:/profil");
        bankAccountService.addBankAccount(user, bankAccount);
        modelAndView.addObject("addBankAccount", bankAccount);
        return modelAndView;
    }

    @PostMapping("/transferToMyBankAccount")
    public ModelAndView transferToMyBankAccount(@RequestParam(value = "id") String strIBANAccount, @RequestParam(value = "amount") double amount, Authentication auth) {

        User user = new User();
        user = userService.findUserByEmail(auth.getName());

        ModelAndView modelAndView = new ModelAndView("redirect:/profil");
        double amountMax = user.getSolde() * (1 - Constante.COMMISSION);
        //modelAndView.addObject("amountMax", amountMax);
        bankAccountService.transferToOrFromMyBankAccount(user, strIBANAccount, amount * -1.0);
        modelAndView.addObject("amountMax", amountMax);
        return modelAndView;
    }

    @PostMapping("/transferFromMyBankAccount")
    public ModelAndView transferFromMyBankAccount(@RequestParam(value = "id") String strIBANAccount, @RequestParam(value = "amount") double amount, Authentication auth) {

        User user = new User();
        user = userService.findUserByEmail(auth.getName());

        ModelAndView modelAndView = new ModelAndView("redirect:/profil");
        bankAccountService.transferToOrFromMyBankAccount(user, strIBANAccount, amount);
        return modelAndView;
    }

}
