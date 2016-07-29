package com.schibsted.service;

import com.schibsted.domain.user.User;

import java.util.List;
import java.util.Optional;


public interface IUserService {

    User loadUserByUsernameAndPassword(String username, String password);

    Optional<User> save(User user);

    Optional<User> update(User userUpdate);

    Optional<User> findByUsername(String username);

    List<User> getUserListByPermission(String permission);

    boolean userHasPermissionsOnPage(User user, String pageName);
}
