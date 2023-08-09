package com.openclassrooms.paymybuddy;

import com.openclassrooms.paymybuddy.model.BankAccount;
import com.openclassrooms.paymybuddy.model.Transaction;
import com.openclassrooms.paymybuddy.model.User;
import com.openclassrooms.paymybuddy.repository.BankAccountRepository;
import com.openclassrooms.paymybuddy.repository.TransactionRepository;
import com.openclassrooms.paymybuddy.service.BankAccountServiceImpl;
import com.openclassrooms.paymybuddy.service.TransactionServiceImpl;
import com.openclassrooms.paymybuddy.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
@ContextConfiguration
@ExtendWith(MockitoExtension.class)
public class BankAccountServiceTest {

    @MockBean
    private TransactionRepository transactionRepository;
    @MockBean
    private BankAccountRepository bankAccountRepository;
    @Autowired
    private BankAccountServiceImpl bankAccountService;
    private User actualUser;
    private User myFriend;
    private BankAccount actualBankAccount;

    private double mySolde;
    @BeforeEach
    void setUp() {

        User myFriend = new User();
        myFriend.setId(2);
        myFriend.setFirstName("firstNameFriendActualUser");
        myFriend.setLastName("lastNameFriendActualUser");
        myFriend.setPassword("passwordFriendActualUser");
        myFriend.setEmail("emailFriendActualUser@gmail.com");
        myFriend.setSolde(0);
        this.myFriend = myFriend;

        mySolde = 2000.00;
        User actualUser = new User();
        actualUser.setId(1);
        actualUser.setFirstName("firstNameActualUser");
        actualUser.setLastName("lastNameActualUser");
        actualUser.setPassword("passwordActualUser");
        actualUser.setEmail("emailActualUser@gmail.com");
        actualUser.setSolde(mySolde);
        List<User> listFriend = actualUser.getFriends();
        listFriend.add(myFriend);
        this.actualUser = actualUser;

        BankAccount actualBankAccount = new BankAccount();
        actualBankAccount.setIban("ibanActualUser");
        actualBankAccount.setDescription("actualDescription");
        actualBankAccount.setUser(actualUser);
        this.actualBankAccount = actualBankAccount;
    }
    @Test
    void addBankAccount() {

        BankAccount newBankAccount = new BankAccount();
        newBankAccount.setIban("newIBAN");
        newBankAccount.setDescription("newDescription");

        bankAccountService.addBankAccount(actualUser, newBankAccount);

        verify(bankAccountRepository, times(1)).save(any(BankAccount.class));
    }

    @Test
    void transferToOrFromMyBankAccount_WhenTransfertToMyBankAccount() {

        double amount = -1000.00;
        String ibanAccount = "ibanActualUser";

        bankAccountService.transferToOrFromMyBankAccount(actualUser, ibanAccount, amount);

        assertThat(actualUser.getSolde()).isEqualTo(995);
        verify(transactionRepository, times(1)).save(any(Transaction.class));
   }

    @Test
    void transferToOrFromMyBankAccount_WhenTransfertFromMyBankAccount() {

        double amount = 1000.00;
        String ibanAccount = "ibanActualUser";

        bankAccountService.transferToOrFromMyBankAccount(actualUser, ibanAccount, amount);

        assertThat(actualUser.getSolde()).isEqualTo(3000);
        verify(transactionRepository, times(1)).save(any(Transaction.class));
    }
}
