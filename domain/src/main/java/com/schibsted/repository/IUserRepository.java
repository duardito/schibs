package com.schibsted.repository;

import com.schibsted.domain.user.User;

/**
 * Created by edu on 23/07/2016.
 */
public interface IUserRepository {

    User loadUserByUsernameAndPassword(String username, String password);
}
