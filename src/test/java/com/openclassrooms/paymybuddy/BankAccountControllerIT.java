package com.openclassrooms.paymybuddy;

import com.openclassrooms.paymybuddy.model.BankAccount;
import com.openclassrooms.paymybuddy.model.User;
import com.openclassrooms.paymybuddy.repository.BankAccountRepository;
import com.openclassrooms.paymybuddy.service.BankAccountServiceImpl;
import com.openclassrooms.paymybuddy.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpHeaders;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration
@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
//@Sql("../../../../../scripts/dataTest.sql")
@Sql("dataTest.sql")
public class BankAccountControllerIT {

    @LocalServerPort
    private int port;
    @Autowired
    private BankAccountServiceImpl bankAccountService;

    @Autowired
    private UserService userService;

    @Autowired
    private BankAccountRepository bankAccountRepository;

    private HttpHeaders httpHeaders = new HttpHeaders();
    private TestRestTemplate restTemplate = new TestRestTemplate();
    private User actualUser;
    private User myFriend;
    private BankAccount actualBankAccount;
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

        double mySolde = 2000.00;
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
    public void addBankAccount() {

    }
}
