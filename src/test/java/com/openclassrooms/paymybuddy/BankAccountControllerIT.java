package com.openclassrooms.paymybuddy;

import com.openclassrooms.paymybuddy.model.BankAccount;
import com.openclassrooms.paymybuddy.model.User;
import com.openclassrooms.paymybuddy.repository.BankAccountRepository;
import com.openclassrooms.paymybuddy.service.BankAccountServiceImpl;
import com.openclassrooms.paymybuddy.service.UserService;
import com.openclassrooms.paymybuddy.service.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ContextConfiguration
@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
//@Sql("../../../../../scripts/dataTest.sql")
@Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "dataTest.sql")
public class BankAccountControllerIT {

    @LocalServerPort
    private int port;
    @Autowired
    private BankAccountServiceImpl bankAccountService;

    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private BankAccountRepository bankAccountRepository;

   /* private HttpHeaders httpHeaders = new HttpHeaders();
    private TestRestTemplate restTemplate = new TestRestTemplate();*/
    private User actualUser;
    private User myFriend;
    private BankAccount actualBankAccount;
    @Autowired
    private MockMvc mockMvc;

    /*@Mock
    private Authentication auth;
    private Authentication authentication;*/

 /*   @BeforeEach
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
        //actualBankAccount.setUser(actualUser);
        this.actualBankAccount = actualBankAccount;

        //authentication = auth;
    }*/
    @Test
    //@WithUserDetails("TestUseremail@gmail.com")
  //  @WithMockUser (username = "TestUseremail@gmail.com", password = "TestUserpassword")
    public void addBankAccount() throws Exception {

     /*   ResultActions perform = mockMvc.perform(post("/bankAccount")
                        .accept(MediaType.ALL)
                        .requestAttr("bankAccount", actualBankAccount))
                .andExpect(status().isOk());*/
                //.param("transferDescription", "Libellé du transfert").param("transferAmout", "13.55")


        // THEN
        //perform.andExpect(status().isFound());

    }

    @Test
    public void getTransactionsTest() throws Exception {
        mockMvc.perform(get("/homepage")
                        .with(user("TestUseremail@gmail.com").password("TestUserpassword")))
                .andExpect(MockMvcResultMatchers.model().attribute("amountMax", 1990.00))
                .andExpect(MockMvcResultMatchers.model().attribute("accountBalance", 2000.00))
                .andReturn();
    }

    @Test
    public void getRegisterTest() throws Exception {
        mockMvc.perform(get("/register")).andExpect(view().name("register"));
    }
}
