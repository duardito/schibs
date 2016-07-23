package com.schibsted.service;

import com.schibsted.domain.user.User;

/**
 * Created by edu on 23/07/2016.
 */
public interface IUserService {

    User loadUserByUsernameAndPassword(String username, String password);
}
