package com.openclassrooms.paymybuddy;

import com.openclassrooms.paymybuddy.model.Transaction;
import com.openclassrooms.paymybuddy.model.User;
import com.openclassrooms.paymybuddy.repository.TransactionRepository;
import com.openclassrooms.paymybuddy.service.TransactionServiceImpl;
import com.openclassrooms.paymybuddy.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@SpringBootTest
@ContextConfiguration
@ExtendWith(MockitoExtension.class)
public class TransactionServiceTest {

    @MockBean
    private TransactionRepository transactionRepository;
    @MockBean
    private UserService userService;
    @Autowired
    private TransactionServiceImpl transactionService;

    private User actualUser;
    private User myFriend;

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
    }
    @Test
    void addTransaction() {

        Transaction transaction = new Transaction();
        double amount = 1000.00;
        String strDescription = "TEST addTransaction";

        when(userService.findUserByEmail(anyString())).thenReturn(myFriend);
        when(userService.findById(1)).thenReturn(Optional.ofNullable(actualUser));
        when(transactionRepository.save(any(Transaction.class))).thenReturn(any(Transaction.class));

        transaction = transactionService.addTransaction(actualUser.getId(), myFriend.getEmail(), strDescription, amount);

        assertThat(transaction.getUserSender().getId()).isEqualTo(actualUser.getId());
        assertThat(transaction.getUserSender().getSolde()).isEqualTo(995.00);
        assertThat(transaction.getUserReceiver().getId()).isEqualTo(myFriend.getId());
        assertThat(transaction.getUserReceiver().getSolde()).isEqualTo(amount);
        assertThat(transaction.getDescription()).isEqualTo(strDescription);

        verify(transactionRepository, times(1)).save(transaction);
        verify(userService, times(1)).saveUser(actualUser);
        verify(userService, times(1)).saveUser(myFriend);
    }
}
