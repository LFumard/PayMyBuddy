package com.openclassrooms.paymybuddy.service;

import com.openclassrooms.paymybuddy.model.BankAccount;
import com.openclassrooms.paymybuddy.model.Transaction;
import com.openclassrooms.paymybuddy.model.User;
import com.openclassrooms.paymybuddy.repository.BankAccountRepository;
import com.openclassrooms.paymybuddy.repository.TransactionRepository;
import com.openclassrooms.paymybuddy.utils.Constante;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
public class BankAccountServiceImpl implements BankAccountService {

    private final BankAccountRepository bankAccountRepository;

    private final TransactionRepository transactionRepository;
    public BankAccountServiceImpl(BankAccountRepository bankAccountRepository, TransactionRepository transactionRepository) {
        this.bankAccountRepository = bankAccountRepository;
        this.transactionRepository = transactionRepository;
    }

    @Override
    public void addBankAccount(User user, BankAccount bankAccount) {
        BankAccount newBankAccount = new BankAccount();
        newBankAccount.setUser(user);
        newBankAccount.setIban(bankAccount.getIban());
        newBankAccount.setDescription(bankAccount.getDescription());
        bankAccountRepository.save(newBankAccount);
    }

    @Override
    public List<BankAccount> findAllByUserId(int idUser) {
        return bankAccountRepository.findAllByUser_id(idUser);
    }

    @Override
    @Transactional
    public void transferToOrFromMyBankAccount(User user, String strIBANAccount, double amount) {

        Double accountBalance;
        Transaction transaction = new Transaction();
        transaction.setUserReceiver(user);
        transaction.setUserSender(user);
        if (amount >= 0.0) {
            transaction.setDescription("Transfer from my IBAN Account " + strIBANAccount);
            accountBalance = amount;
        }
        else {
            transaction.setDescription(("Transfer to my IBAN Account " + strIBANAccount));
            accountBalance = Math.round(amount * (1 + Constante.COMMISSION) * 100.00) / 100.00;
        }
        transaction.setAmount(amount);
        transactionRepository.save(transaction);
        user.setSolde(user.getSolde() + accountBalance);
    }
}
