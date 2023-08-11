package com.openclassrooms.paymybuddy.controller;

import com.openclassrooms.paymybuddy.model.BankAccount;
import com.openclassrooms.paymybuddy.repository.UserRepository;
import com.openclassrooms.paymybuddy.service.BankAccountService;
import com.openclassrooms.paymybuddy.utils.Constante;
import com.openclassrooms.paymybuddy.model.User;
import com.openclassrooms.paymybuddy.service.TransactionService;
import com.openclassrooms.paymybuddy.service.UserService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Optional;

@Controller
public class UserController {
    private User user;

    public UserController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private final TransactionService transactionService;

    @Autowired
    private BankAccountService bankAccountService;

    @Autowired
    PasswordEncoder passwordEncoder;

    @GetMapping("/login")
    public String login(Model model) {
        user = new User();
        model.addAttribute("user", user);
        return "login";
    }

    @GetMapping("/register")
    public String register(Model model) {
        user = new User();
        model.addAttribute("user", user);
        return "register";
    }


    @PostMapping("/register")
    public ModelAndView saveUser(@ModelAttribute User user, Model model) {

        if (userService.userExist(user.getEmail())) {
            model.addAttribute("logError", "logError");
            return new ModelAndView("/register");
        }
        System.out.println(user);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userService.saveUser(user);
        model.addAttribute("user", new User());
        return new ModelAndView("/login");
    }

    @PostMapping("/save")
    //public ModelAndView UpdateUser(@ModelAttribute User localUser, @NotNull Authentication auth, final ModelAndView modelAndView) {
    public ModelAndView UpdateUser(@ModelAttribute User localUser, @NotNull Authentication auth) {

        Optional<User> user = Optional.ofNullable(userService.findUserByEmail(auth.getName()));
        userService.updateProfilUser(auth.getName(), localUser);
        return new ModelAndView("redirect:/profil");
    }


    @GetMapping("/contact")
    public ModelAndView getContact(final ModelAndView modelAndView, Authentication auth) {

        Optional<User> user = Optional.of(new User());
        user = Optional.ofNullable(userService.findUserByEmail(auth.getName()));
        modelAndView.addObject("friendsList", userService.findAllFriendsById(user.get().getId()));

        return modelAndView;
    }

    @PostMapping("/contact")
    public ModelAndView saveFriend(final ModelAndView actuelmodelAndView, Authentication auth,
                                   @RequestParam(value = "email") final String email) {

        Optional<User> user = Optional.of(new User());
        user = Optional.ofNullable(userService.findUserByEmail(auth.getName()));

        final ModelAndView modelAndView = new ModelAndView("/contact");

        modelAndView.addObject("addContact", userService.addContact(email, user.get().getId()));
        modelAndView.addObject("friendsList", userService.findAllFriendsById(user.get().getId()));
        return modelAndView;
    }

    @GetMapping("/profil")
    public ModelAndView profile(Authentication auth,
                                @RequestParam(name = "page", defaultValue = "1") int page,
                                @RequestParam(name = "size", defaultValue = "5") int size,
                                ModelAndView modelAndView) {

        Optional<User> user = Optional.of(new User());
        user = Optional.ofNullable(userService.findUserByEmail(auth.getName()));

        List<BankAccount> bankAccounts = bankAccountService.findAllByUserId(user.get().getId());

        modelAndView.addObject("user", user.get());
        modelAndView.addObject("accountBalance", user.get().getSolde());

        double amountMax = (double) Math.round(user.get().getSolde() * (1 - Constante.COMMISSION) * 100) / 100;

        modelAndView.addObject("amountMax", amountMax);
        modelAndView.addObject("transactions", transactionService.findAllByReceiverId(user.get().getId(), PageRequest.of(page - 1, size, Sort.by("Id").descending())));
        modelAndView.addObject("listBankAccount", bankAccountService.findAllByUserId(user.get().getId()));
        modelAndView.addObject("addBankAccount", new BankAccount());

        return modelAndView;
    }
}
