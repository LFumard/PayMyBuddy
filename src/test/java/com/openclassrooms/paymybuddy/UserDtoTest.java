package com.openclassrooms.paymybuddy;

import com.openclassrooms.paymybuddy.Dto.UserDto;
import com.openclassrooms.paymybuddy.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ContextConfiguration
@ExtendWith(MockitoExtension.class)
public class UserDtoTest {

    private User actualUser;
    @BeforeEach
    void setUp() {

        User actualUser = new User();
        actualUser.setId(1);
        actualUser.setFirstName("firstNameActualUser");
        actualUser.setLastName("lastNameActualUser");
        actualUser.setEmail("emailActualUser@gmail.com");
        actualUser.setSolde(1000.00);
        this.actualUser = actualUser;
    }

    @Test
    public void fromEntityUser() {

        UserDto userDtotest;
        userDtotest = UserDto.fromEntityUser(actualUser);

        assertThat(userDtotest.getNom()).isEqualTo(actualUser.getLastName());
        assertThat(userDtotest.getPrenom()).isEqualTo(actualUser.getFirstName());
        assertThat(userDtotest.getSolde()).isEqualTo(actualUser.getSolde());
        assertThat(userDtotest.getEmail()).isEqualTo(actualUser.getEmail());
    }
}
