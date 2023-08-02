package com.openclassrooms.paymybuddy.service;

import com.openclassrooms.paymybuddy.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;
import java.util.Optional;

public interface UserService {

	Optional<User> findById(final int id);
	public void saveUser(User user);
	public boolean userExist(String email);
	public String login(String email, String password);

    User findUserByEmail(String connection);

//	Optional<User> findAllFriendsByUserName(String username);
	public UserDetails loadUserByUsername(String username);

//	public Page<User> findAllFriendsByIdPage(int idUserLog, Pageable pageableParam);

	String addContact(String email, int idUserLog);

	User updateUser(String userName, User user);
	//void delete(final int id);

	public List<User> findAllFriendsById(int id);
	//Page<User> findAllFriendsById(int id, Pageable pageable);

	void updateProfilUser(String nameUser, User user);
}
