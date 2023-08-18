package com.openclassrooms.paymybuddy;

import com.openclassrooms.paymybuddy.model.Transaction;
import com.openclassrooms.paymybuddy.repository.TransactionRepository;
import com.openclassrooms.paymybuddy.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ContextConfiguration
@ExtendWith(MockitoExtension.class)
@Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "/dataTest.sql")
public class TransactionControllerITTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    UserRepository userRepository;
    @Autowired
    TransactionRepository transactionRepository;
    @Test
    public void getTransactionsTest() throws Exception {

        Page<Transaction> lstTransactionUser = transactionRepository.findAllByUserSender_id(userRepository.findUserByEmail("TestUseremail@gmail.com").getId(), PageRequest.of(0, 5, Sort.by("Id").descending()));

        mockMvc.perform(get("/transfer")
                        .with(user("TestUseremail@gmail.com").password("TestUserpassword")))
                .andExpectAll(
                        view().name("transfer"),
                        status().isOk(),
                        model().attribute("amountMax", 1990.00),
                        model().attribute("transactions", lstTransactionUser)
                )
                .andReturn();
    }

    @Test
    public void postTransactionsTest() throws Exception {

        mockMvc.perform(post("/transfer/saveTransaction")
                        .with(user("TestUseremail@gmail.com").password("TestUserpassword"))
                        .param("connection", "TestFriendUseremail@gmail.com")
                        .param("amount", "1000.00")
                        .param("description", "New Test Transaction for UserTest"))
                .andExpectAll(
                        view().name("redirect:/transfer")
                )
                .andReturn();

        double newUserSolde = userRepository.findUserByEmail("TestUseremail@gmail.com").getSolde();
        assertThat(newUserSolde).isEqualTo(995.00);

        double newFriendUserSolde = userRepository.findUserByEmail("TestFriendUseremail@gmail.com").getSolde();
        assertThat(newFriendUserSolde).isEqualTo(2000.00);
    }
}
