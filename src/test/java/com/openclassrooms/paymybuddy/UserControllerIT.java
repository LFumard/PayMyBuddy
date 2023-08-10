package com.openclassrooms.paymybuddy;

import com.openclassrooms.paymybuddy.Dto.UserDto;
import com.openclassrooms.paymybuddy.model.User;
import com.openclassrooms.paymybuddy.repository.UserRepository;
import org.junit.Assert;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.web.servlet.function.RequestPredicates.param;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ContextConfiguration
@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
@Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "dataTest.sql")
public class UserControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    UserRepository userRepository;
    @Test
    public void getRegisterTest() throws Exception {

        mockMvc.perform(get("/register")
        ) //.with(user("TestUseremail@gmail.com").password("TestUserpassword")))
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
        assertThat(userInBdD).isEqualTo(expectedUser);

    }
}
