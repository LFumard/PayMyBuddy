package com.openclassrooms.paymybuddy.service;

import com.openclassrooms.paymybuddy.model.User;
import com.openclassrooms.paymybuddy.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService, UserDetailsService {

    private final UserRepository userRepository;

    public UserServiceImpl(final UserRepository myuserRepository) {
        userRepository = myuserRepository;
    }

    public void saveUser(User user) {

        userRepository.save(user);
    }

    public boolean userExist(String email) {

        return (userRepository.findByEmail(email).isPresent());
    }

    @Override
    public String login(String email, String password) {
        return email;
    }

    @Override
    public User findUserByEmail(final String email) {
        return userRepository.findUserByEmail(email);
    }

    @Override
    public Optional<User> findById(final int id) {
        return userRepository.findById(id);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // supplier
        return userRepository.findByEmail(username).orElseThrow(() -> new UsernameNotFoundException(username));
    }


    @Override
    public String addContact(String email, int idUser) {
        Optional<User> user = this.findById(idUser);
        Optional<User> friend = Optional.ofNullable(this.findUserByEmail(email));

        if (friend.isPresent()) {
            List<User> listFriend = user.get().getFriends();
            if (!listFriend.contains(friend.get())) {
                listFriend.add(friend.get());
                user.get().setFriends(listFriend);
                this.updateUser(user.get().getEmail(), user.get());
                return "";
            } else
                return ("This contact is already in you're friend's list");
        } else
            return ("This contact doesn't exist");
    }

    @Override
    public User updateUser(String nameUser, User user) {
        User userLocal = userRepository.findByEmail(nameUser).orElseThrow(() -> new RuntimeException("user doesn't exist"));
        userLocal.setFirstName(user.getFirstName());
        userLocal.setLastName(user.getLastName());
        userLocal.setEmail(user.getEmail());
        userLocal.setSolde(user.getSolde());
        userLocal.setFriends(user.getFriends());
        userLocal.setTransactionsEmit(user.getTransactionsEmit());
        userLocal.setTransactionsReceiver(user.getTransactionsReceiver());
        return userRepository.save(userLocal);
    }


    @Override
    public List<User> findAllFriendsById(int id) {
        return userRepository.findFriends(id);
    }



    @Override
    public void updateProfilUser(String nameUser, User user) {
        User userLocal = userRepository.findByEmail(nameUser).orElseThrow(() -> new RuntimeException("user doesn't exist"));
        userLocal.setFirstName(user.getFirstName());
        userLocal.setLastName(user.getLastName());
        userRepository.save(userLocal);
    }
}
