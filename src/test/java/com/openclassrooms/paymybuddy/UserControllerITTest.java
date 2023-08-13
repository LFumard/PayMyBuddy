package com.openclassrooms.paymybuddy;

import com.openclassrooms.paymybuddy.model.BankAccount;
import com.openclassrooms.paymybuddy.model.User;
import com.openclassrooms.paymybuddy.repository.BankAccountRepository;
import com.openclassrooms.paymybuddy.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ContextConfiguration
@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
@Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "/dataTest.sql")
public class UserControllerITTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    UserRepository userRepository;
    @Autowired
    BankAccountRepository bankAccountRepository;
/*
    @Autowired
    TransactionRepository transactionRepository;
*/
    @Test
    public void getRegisterTest() throws Exception {

        mockMvc.perform(get("/register"))
                .andExpectAll(
                        status().isOk(),
                        content().contentType(MediaType.TEXT_HTML_VALUE + ";charset=UTF-8"),
                        view().name("register"),
                        model().attribute("user", new User())
                )
                .andReturn();
    }

    @Test
    public void getloginTest() throws Exception {

        mockMvc.perform(get("/login"))
                .andExpectAll(
                        status().isOk(),
                        content().contentType(MediaType.TEXT_HTML_VALUE + ";charset=UTF-8"),
                        view().name("login"),
                        model().attribute("user", new User())
                )
                .andReturn();
    }

    @Test
    public void postRegisterTest_WhenUserNotExist() throws Exception {

        mockMvc.perform(post("/register")
                        .contentType( MediaType.parseMediaType("application/x-www-form-urlencoded"))
                        .param("lastName", "TestNewUserlastname")
                        .param("firstName", "TestNewUserfirstname")
                        .param("email", "TestNewUseremail@gmail.com")
                        .param("password", "TestNewUserpassword")
                )
                .andExpect(status().isOk())
                .andExpect(view().name("/login"));

        User expectedUser = new User();
        expectedUser.setLastName("TestNewUserlastname");
        expectedUser.setFirstName("TestNewUserfirstname");
        expectedUser.setEmail("TestNewUseremail@gmail.com");
        expectedUser.setSolde(0.00);

        User userInBdD = userRepository.findUserByEmail("TestNewUseremail@gmail.com");
        assertThat(userInBdD.getLastName()).isEqualTo(expectedUser.getLastName());
        assertThat(userInBdD.getFirstName()).isEqualTo(expectedUser.getFirstName());
        assertThat(userInBdD.getEmail()).isEqualTo(expectedUser.getEmail());
        assertThat(userInBdD.getSolde()).isEqualTo(expectedUser.getSolde());

        List<User> totalUserInBdD = userRepository.findAll();
        assertThat(totalUserInBdD.size()).isEqualTo(5);
    }

    @Test
    public void postRegisterTest_WhenUserExist() throws Exception {

        mockMvc.perform(post("/register")
                        .contentType( MediaType.parseMediaType("application/x-www-form-urlencoded"))
                        .param("lastName", "TestNewUserlastname")
                        .param("firstName", "TestNewUserfirstname")
                        .param("email", "TestUseremail@gmail.com")
                        .param("password", "TestNewUserpassword")
                )
                .andExpect(status().isOk())
                .andExpect(model().attribute("logError","logError"))
                .andExpect(view().name("/register"));

        List<User> totalUserInBdD = userRepository.findAll();
        assertThat(totalUserInBdD.size()).isEqualTo(4);
    }

    @Test
    public void postSaveTest() throws Exception {

        User expectedUser = new User();
        expectedUser.setId(userRepository.findByEmail("TestUseremail@gmail.com").get().getId());
        expectedUser.setLastName("TestNewUserlastname");
        expectedUser.setFirstName("TestNewUserfirstname");
        expectedUser.setEmail("TestUseremail@gmail.com");
        expectedUser.setSolde(2000.00);

        mockMvc.perform(post("/save")
                        .with(user("TestUseremail@gmail.com").password("TestUserpassword"))
                        .param("lastName", "TestNewUserlastname")
                        .param("firstName", "TestNewUserfirstname")
                        .param("email", "TestUseremail@gmail.com")
                        .with(csrf()))
                .andExpectAll(
                        view().name("redirect:/profil")
                )
                .andReturn();
        User userUpdated = userRepository.findUserByEmail("TestUseremail@gmail.com");
        assertThat(userUpdated.getLastName().equals(expectedUser.getLastName()));
        assertThat(userUpdated.getFirstName().equals(expectedUser.getFirstName()));
    }

    @Test
    public void getContactTest() throws Exception {

        User expectedFriendUser = userRepository.findUserByEmail("TestFriendUseremail@gmail.com");
        List<User> lstExpectedFriendUser = new ArrayList<>();
        lstExpectedFriendUser.add(expectedFriendUser);

        mockMvc.perform(get("/contact")
                        .with(user("TestUseremail@gmail.com").password("TestUserpassword")))
                .andExpectAll(
                        view().name("contact"),
                        status().isOk(),
                        model().attribute("friendsList", lstExpectedFriendUser)
                )
                .andReturn();
    }

    @Test
    public void postContactTest() throws Exception {

        User friendUser = userRepository.findUserByEmail("TestFriendUseremail@gmail.com");
        User newfriendUser = userRepository.findUserByEmail("TestANewFriendUseremail@gmail.com");
        List<User> lstExpectedFriendUser = new ArrayList<>();
        lstExpectedFriendUser.add(friendUser);
        lstExpectedFriendUser.add(newfriendUser);

        mockMvc.perform(post("/contact")
                        .with(user("TestUseremail@gmail.com").password("TestUserpassword"))
                        .param("email", "TestANewFriendUseremail@gmail.com"))
                .andExpectAll(
                        view().name("/contact"),
                        status().isOk(),
                        model().attribute("friendsList", lstExpectedFriendUser)
                )
                .andReturn();
        List<User> lstUserInBdD = userRepository.findFriends(userRepository.findByEmail("TestUseremail@gmail.com").get().getId());
        assertThat(lstUserInBdD.size()).isEqualTo(2);
    }

    @Test
    public void getProfilTest() throws Exception {

        List<BankAccount> lstBankAccountUser; // = new ArrayList<>();
        lstBankAccountUser = bankAccountRepository.findAllByUser_id(userRepository.findUserByEmail("TestUseremail@gmail.com").getId());

        mockMvc.perform(get("/profil")
                        .with(user("TestUseremail@gmail.com").password("TestUserpassword")))
                .andExpectAll(
                        view().name("profil"),
                        status().isOk(),
                        model().attribute("amountMax", 1990.00),
                        model().attribute("listBankAccount", lstBankAccountUser)
                )
                .andReturn();
    }
}
