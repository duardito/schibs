package com.schibsted.service;

import com.schibsted.domain.user.User;

import java.util.Optional;

/**
 * Created by edu on 23/07/2016.
 */
public interface IUserService {

    Optional<User> loadUserByUsernameAndPassword(String username, String password);

    User save(User user);

    User update(User userUpdate);

    Optional<User> findByUsername(String username);
}
