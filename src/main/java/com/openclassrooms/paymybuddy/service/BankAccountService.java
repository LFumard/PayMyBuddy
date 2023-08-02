package com.openclassrooms.paymybuddy.service;

import com.openclassrooms.paymybuddy.model.BankAccount;
import com.openclassrooms.paymybuddy.model.User;

import java.util.List;

public interface BankAccountService {
    void addBankAccount(User user, BankAccount bankAccount);

    List<BankAccount> findAllByUserId(int idUser);

    void transferToOrFromMyBankAccount(User user, String strIBANAccount, double amount);
}
