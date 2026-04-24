package com.carrental.dao;

import com.carrental.model.User;
import com.carrental.model.UserCredential;
import java.util.List;
import java.util.Optional;

public interface UserDAO {
    void registerUser(User user, UserCredential credential);
    Optional<User> findById(int userId);
    Optional<User> findByEmail(String email);
    Optional<UserCredential> findCredentialByUsername(String username);
    List<User> findAll();
    void updateUser(User user);
    void deleteUser(int userId);
}
