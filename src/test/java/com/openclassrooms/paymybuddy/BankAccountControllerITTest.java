package com.openclassrooms.paymybuddy;

import com.openclassrooms.paymybuddy.model.BankAccount;
import com.openclassrooms.paymybuddy.repository.BankAccountRepository;
import com.openclassrooms.paymybuddy.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ContextConfiguration
@ExtendWith(MockitoExtension.class)
@Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "/dataTest.sql")
public class BankAccountControllerITTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    UserRepository userRepository;
    @Autowired
    BankAccountRepository bankAccountRepository;
    @Test
    public void postAddBankAccountTest() throws Exception {

        mockMvc.perform(post("/bankAccount")
                        .with(user("TestUseremail@gmail.com").password("TestUserpassword"))
                        .contentType( MediaType.parseMediaType("application/x-www-form-urlencoded"))
                        .param("iban", "NewIBANBankAccountUserTest")
                        .param("description", "Add Secondary Bank Account For User Test")
                );

        List<BankAccount> lstBankAccountInBdD; // = new ArrayList<>();
        lstBankAccountInBdD = bankAccountRepository.findAllByUser_id(userRepository.findUserByEmail("TestUseremail@gmail.com").getId());
        assertThat(lstBankAccountInBdD.size()).isEqualTo(2);
        assertThat(lstBankAccountInBdD.get(1).getIban()).isEqualTo("NewIBANBankAccountUserTest");
    }

    @Test
    public void postTransfertToMyBankAccountTest() throws Exception {

        mockMvc.perform(post("/bankAccount/transferToMyBankAccount")
                .with(user("TestUseremail@gmail.com").password("TestUserpassword"))
                .contentType( MediaType.parseMediaType("application/x-www-form-urlencoded"))
                .queryParam("id", "16")
                .queryParam("amount", "1000.00"))
                .andExpectAll(
                        view().name("redirect:/profil")
                )
                .andReturn();

        double newUserSolde = userRepository.findUserByEmail("TestUseremail@gmail.com").getSolde();
        assertThat(newUserSolde).isEqualTo(995.00);
    }

    @Test
    public void postTransfertFromMyBankAccountTest() throws Exception {

        mockMvc.perform(post("/bankAccount/transferFromMyBankAccount")
                        .with(user("TestUseremail@gmail.com").password("TestUserpassword"))
                        .contentType( MediaType.parseMediaType("application/x-www-form-urlencoded"))
                        .queryParam("id", "16")
                        .queryParam("amount", "5000.00"))
                .andExpectAll(
                        view().name("redirect:/profil")
                )
                .andReturn();

        double newUserSolde = userRepository.findUserByEmail("TestUseremail@gmail.com").getSolde();
        assertThat(newUserSolde).isEqualTo(7000.00);
    }
}
