package com.schibsted.service;

import com.schibsted.domain.user.User;
import com.schibsted.repository.IUserRepository;
import com.schibsted.repository.UserRepositoryImpl;

import java.util.Optional;

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

    @Override
    public Optional<User> loadUserByUsernameAndPassword(final String username, String password) {
        return Optional.ofNullable(userRepository.loadUserByUsernameAndPassword(username,password));
    }

    @Override
    public User save(final User user) {
        return userRepository.save(user);
    }

    @Override
    public User update(final User userUpdate) {
        return userRepository.update(userUpdate);
    }

    @Override
    public Optional <User> findByUsername(final String username){
        return Optional.ofNullable(userRepository.findByUsername(username));
    }
}
