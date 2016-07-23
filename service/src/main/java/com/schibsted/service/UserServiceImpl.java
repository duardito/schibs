package com.schibsted.service;

import com.schibsted.domain.user.User;
import com.schibsted.repository.IUserRepository;
import com.schibsted.repository.UserRepositoryImpl;

/**
 * Created by edu on 23/07/2016.
 */
public class UserServiceImpl implements IUserService {

    private IUserRepository userRepository;

    public UserServiceImpl(){
        if(userRepository == null){
            userRepository = new UserRepositoryImpl();
        }
    }

    public User loadUserByUsernameAndPassword(final String username, String password) {
        return userRepository.loadUserByUsernameAndPassword(username,password);
    }

}
