package com.openclassrooms.paymybuddy;

import com.openclassrooms.paymybuddy.model.User;
import com.openclassrooms.paymybuddy.repository.UserRepository;
import com.openclassrooms.paymybuddy.service.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@SpringBootTest
@ContextConfiguration
@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @MockBean
    private UserRepository userRepository;
    @Autowired
    private UserServiceImpl userService;

    private User actualUser;
    private User myFriend;

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

        User actualUser = new User();
        actualUser.setId(1);
        actualUser.setFirstName("firstNameActualUser");
        actualUser.setLastName("lastNameActualUser");
        actualUser.setPassword("passwordActualUser");
        actualUser.setEmail("emailActualUser@gmail.com");
        actualUser.setSolde(100);
        //actualUser.setFriends(List.of(myFriend));
        List<User> listFriend = actualUser.getFriends();
        listFriend.add(myFriend);
        this.actualUser = actualUser;
    }
    @Test
    public void addContactTest() {

        User friendUserToAdd = new User();
        friendUserToAdd.setId(10);
        friendUserToAdd.setFirstName("firstNamefriendUserToAdd");
        friendUserToAdd.setLastName("lastNamefriendUserToAdd");
        friendUserToAdd.setPassword("passwordfriendUserToAdd");
        friendUserToAdd.setEmail("emailfriendUserToAdd@gmail.com");
        friendUserToAdd.setSolde(0);

        when(userRepository.findById(anyInt())).thenReturn(Optional.of(actualUser));
        when(userRepository.findUserByEmail(friendUserToAdd.getEmail())).thenReturn(friendUserToAdd);
        when(userRepository.findByEmail(actualUser.getEmail())).thenReturn(Optional.ofNullable(actualUser));
        when(userRepository.save(actualUser)).thenReturn(actualUser);

        String result = userService.addContact(friendUserToAdd.getEmail(), actualUser.getId());

        assertThat(result).isEmpty();
        assertThat(actualUser.getFriends().stream().count()).isEqualTo(2);
        assertThat(actualUser.getFriends().get(1).toString()).isEqualTo(friendUserToAdd.toString());
        verify(userRepository, times(1)).save(actualUser);
    }

    @Test
    public void addContactTestWhenEMailFriendDoesNotExist() {

        //Optional<User> friendUserToAdd = Optional.of(new User());
        User friendUserToAdd = new User();
        friendUserToAdd.setId(10);
        friendUserToAdd.setFirstName("firstNamefriendUserToAdd");
        friendUserToAdd.setLastName("lastNamefriendUserToAdd");
        friendUserToAdd.setPassword("passwordfriendUserToAdd");
        friendUserToAdd.setEmail("emailfriendUserToAdd@gmail.com");
        friendUserToAdd.setSolde(0);

        when(userRepository.findById(anyInt())).thenReturn(Optional.of(actualUser));
        when(userRepository.findUserByEmail(friendUserToAdd.getEmail())).thenReturn(null);
        when(userRepository.findByEmail(actualUser.getEmail())).thenReturn(Optional.ofNullable(actualUser));
        when(userRepository.save(actualUser)).thenReturn(actualUser);

        String result = userService.addContact(friendUserToAdd.getEmail(), actualUser.getId());

        assertThat(result).isEqualTo("This contact doesn't exist");
        assertThat(actualUser.getFriends().stream().count()).isEqualTo(1);
        verify(userRepository, times(0)).save(actualUser);
    }

    @Test
    public void addContactTestWhenFriendAlreadyAFriend() {

        User friendUserToAdd = new User();
        friendUserToAdd.setId(2);
        friendUserToAdd.setFirstName("firstNameFriendActualUser");
        friendUserToAdd.setLastName("lastNameFriendActualUser");
        friendUserToAdd.setPassword("passwordFriendActualUser");
        friendUserToAdd.setEmail("emailFriendActualUser@gmail.com");
        friendUserToAdd.setSolde(0);

        when(userRepository.findById(anyInt())).thenReturn(Optional.of(actualUser));
        when(userRepository.findUserByEmail(friendUserToAdd.getEmail())).thenReturn(friendUserToAdd);
        when(userRepository.findByEmail(actualUser.getEmail())).thenReturn(Optional.ofNullable(actualUser));
        when(userRepository.save(actualUser)).thenReturn(actualUser);

        //String result = userService.addContact(friendUserToAdd.getEmail(), actualUser.getId());
        String result = userService.addContact(myFriend.getEmail(), actualUser.getId());

        assertThat(result).isEqualTo("This contact is already in you're friend's list");
        assertThat(actualUser.getFriends().stream().count()).isEqualTo(1);
        verify(userRepository, times(0)).save(actualUser);
    }

    @Test
    public void updateProfilUser() {

        actualUser.setFirstName("NEWfirstNameActualUser");
        actualUser.setLastName("NEWlastNameActualUser");

        when(userRepository.findByEmail(actualUser.getEmail())).thenReturn(Optional.ofNullable(actualUser));
        when(userRepository.save(actualUser)).thenReturn(actualUser);

        userService.updateProfilUser(actualUser.getEmail(), actualUser);

        verify(userRepository, times(1)).save(actualUser);
    }

    @Test
    public void updateUser() {

        actualUser.setFirstName("NEWfirstNameActualUser");
        actualUser.setLastName("NEWlastNameActualUser");
        actualUser.setSolde(1000.00);

        when(userRepository.findByEmail(actualUser.getEmail())).thenReturn(Optional.ofNullable(actualUser));
        when(userRepository.save(actualUser)).thenReturn(actualUser);

        userService.updateUser(actualUser.getEmail(), actualUser);

        verify(userRepository, times(1)).save(actualUser);
    }
}
