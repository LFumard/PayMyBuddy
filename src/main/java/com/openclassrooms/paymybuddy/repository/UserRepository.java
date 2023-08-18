package com.openclassrooms.paymybuddy.repository;

import com.openclassrooms.paymybuddy.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {

	Optional<User> findByEmail(String email);
    User findUserByEmail(String email);
    @Query("SELECT u.friends FROM User u WHERE u.id = :id")
    List<User> findFriends(final int id);
    @Query(value = "SELECT U.friends FROM User U WHERE U.id = :id",
            countQuery  = "SELECT count(U.friends) FROM User U WHERE U.id = :id")
    Page<User> findFriendsPage(int id, Pageable pageableParam);
}
