package com.openclassrooms.paymybuddy;

import com.openclassrooms.paymybuddy.model.BankAccount;
import com.openclassrooms.paymybuddy.model.User;
import com.openclassrooms.paymybuddy.repository.BankAccountRepository;
import com.openclassrooms.paymybuddy.service.BankAccountServiceImpl;
import com.openclassrooms.paymybuddy.service.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ContextConfiguration
@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
@Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "dataTest.sql")
public class HomeControllerIT {

    @LocalServerPort
    private int port;

    @Autowired
    private MockMvc mockMvc;


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
