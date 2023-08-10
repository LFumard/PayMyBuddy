package com.openclassrooms.paymybuddy.controller;

import com.openclassrooms.paymybuddy.Dto.UserDto;
import com.openclassrooms.paymybuddy.utils.Constante;
import com.openclassrooms.paymybuddy.model.User;
import com.openclassrooms.paymybuddy.service.TransactionService;
import com.openclassrooms.paymybuddy.service.UserService;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;


import java.util.Optional;

@Controller
public class HomeController {

    private UserService userService;
    private final TransactionService transactionService;

    public HomeController(UserService userService, TransactionService transactionService) {
        this.userService = userService;
        this.transactionService = transactionService;
    }

    @GetMapping("/homepage")
    public ModelAndView getUtilisateur(Authentication auth,
                                       @RequestParam(name = "page", defaultValue = "1") int page,
                                       @RequestParam(name = "size", defaultValue = "10") int size) {

        Optional<User> user = Optional.of(new User());
        user = Optional.ofNullable(userService.findUserByEmail(auth.getName()));

        ModelAndView modelAndView = new ModelAndView("homepage");
        double amountMax = user.get().getSolde() * (1 - Constante.COMMISSION);

        modelAndView.addObject("amountMax", amountMax);
        modelAndView.addObject("accountBalance", user.get().getSolde());
        modelAndView.addObject("transactions", transactionService.findAllByEmitterAndReceiverId(user.get().getId(), PageRequest.of(page - 1, size)));
        modelAndView.addObject("user", user.map(UserDto::fromEntityUser).orElse(null));
        modelAndView.addObject("breadcrumb", "");

        return modelAndView;
    }
}
