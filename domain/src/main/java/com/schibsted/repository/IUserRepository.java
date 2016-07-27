package com.schibsted.repository;

import com.schibsted.domain.user.User;

import java.util.List;

/**
 * Created by edu on 23/07/2016.
 */
public interface IUserRepository {

    User loadUserByUsernameAndPassword(String username, String password);

    User save(User user);

    User update(User userUpdate);

    User findByUsername(String username);

    List<User> getUserListByPermission(String permission);
}
