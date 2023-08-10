package com.openclassrooms.paymybuddy;

import com.openclassrooms.paymybuddy.Dto.UserDto;
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

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ContextConfiguration
@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
@Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "dataTest.sql")
public class HomeControllerIT {

/*
    @LocalServerPort
    private int port;
*/
    @Autowired
    private MockMvc mockMvc;

    @Test
    public void getTransactionsTest() throws Exception {

        mockMvc.perform(get("/homepage")
                        .with(user("TestUseremail@gmail.com").password("TestUserpassword")))
                .andExpectAll(
                        status().isOk(),
                        content().contentType(MediaType.TEXT_HTML_VALUE + ";charset=UTF-8"),
                        view().name("homepage"),
                        model().attribute("user", new UserDto("TestUserlast_name", "TestUserfirst_name", "TestUseremail@gmail.com", 2000.0)),
                        //model().attribute("transactions", new Page<Transaction>()),
                        model().attribute("amountMax", 1990.00),
                        model().attribute("breadcrumb", "")
                )
                .andReturn();
    }
}
