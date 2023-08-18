package com.openclassrooms.paymybuddy.controller;

import com.openclassrooms.paymybuddy.utils.Constante;
import com.openclassrooms.paymybuddy.Dto.TransactionDTO;
import com.openclassrooms.paymybuddy.model.User;
import com.openclassrooms.paymybuddy.service.TransactionService;
import com.openclassrooms.paymybuddy.service.UserService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import java.util.Optional;

@Controller
@RequestMapping("transfer")
public class TransactionController {

    @Autowired
    private final UserService userService;

    @Autowired
    private final TransactionService transactionService;

    public TransactionController(UserService userService, TransactionService transactionService) {
        this.userService = userService;
        this.transactionService = transactionService;
    }

    @GetMapping
    public ModelAndView getTransactions(@NotNull Authentication auth,
                                        @RequestParam(name = "page", defaultValue = "1") int page,
                                        @RequestParam(name = "size", defaultValue = "5") int size) {

        ModelAndView modelAndView = new ModelAndView("transfer");

        Optional<User> user = Optional.of(new User());
        user = Optional.ofNullable(userService.findUserByEmail(auth.getName()));
        double amountMax = (double) Math.round(user.get().getSolde() * (1 - Constante.COMMISSION) * 100) / 100;

        modelAndView.addObject("amountMax", amountMax);
        modelAndView.addObject("accountBalance", user.get().getSolde());
        modelAndView.addObject("friendList", userService.findAllFriendsById(user.get().getId()));
        modelAndView.addObject("transactions", transactionService.findAllByEmitterId(user.get().getId(), PageRequest.of(page - 1, size, Sort.by("Id").descending())));
        modelAndView.addObject("postTransaction", new TransactionDTO());
        return modelAndView;
    }

    @PostMapping(value = "/saveTransaction")
    public ModelAndView saveTransaction(@ModelAttribute final TransactionDTO transactionParam, Authentication auth) {
        ModelAndView modelAndView = new ModelAndView("redirect:/transfer");
        modelAndView.addObject("postTransaction", transactionParam);

        Optional<User> user = Optional.of(new User());
        user = Optional.ofNullable(userService.findUserByEmail(auth.getName()));

        transactionService.addTransaction(user.get().getId(), transactionParam.getConnection(), transactionParam.getDescription(), transactionParam.getAmount());

        return modelAndView;
    }
}
