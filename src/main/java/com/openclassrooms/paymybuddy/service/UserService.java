package com.openclassrooms.paymybuddy.service;

import com.openclassrooms.paymybuddy.model.User;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.List;
import java.util.Optional;

public interface UserService {

	Optional<User> findById(final int id);
	public void saveUser(User user);
	public boolean userExist(String email);
	public String login(String email, String password);

    User findUserByEmail(String connection);

	public UserDetails loadUserByUsername(String username);

	String addContact(String email, int idUserLog);

	User updateUser(String userName, User user);

	public List<User> findAllFriendsById(int id);

	void updateProfilUser(String nameUser, User user);
}
