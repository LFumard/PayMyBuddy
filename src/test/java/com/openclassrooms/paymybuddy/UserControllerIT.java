package com.openclassrooms.paymybuddy;

import com.openclassrooms.paymybuddy.Dto.UserDto;
import com.openclassrooms.paymybuddy.model.User;
import com.openclassrooms.paymybuddy.repository.UserRepository;
import org.hamcrest.core.IsNull;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
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
        assertThat(userInBdD).isEqualTo(expectedUser);

        List<User> totalUserInBdD = userRepository.findAll();
        assertThat(totalUserInBdD.size()).isEqualTo(2);
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
        assertThat(totalUserInBdD.size()).isEqualTo(1);
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
}
