package com.schibsted.service;

import com.schibsted.common.Constants;
import com.schibsted.domain.user.User;
import com.schibsted.repository.IUserRepository;
import com.schibsted.repository.UserRepositoryImpl;

import java.util.List;
import java.util.Optional;

/**
 * Created by edu on 23/07/2016.
 */
public class UserServiceImpl implements IUserService {

    private IUserRepository userRepository;

    public UserServiceImpl() {
        if (userRepository == null) {
            userRepository = new UserRepositoryImpl();
        }
    }

    @Override
    public User loadUserByUsernameAndPassword(final String username, String password) {
        return userRepository.loadUserByUsernameAndPassword(username, password);
    }

    @Override
    public Optional<User> save(final User user) {
        return Optional.ofNullable(userRepository.save(user));
    }

    @Override
    public Optional<User> update(final User userUpdate) {
        return Optional.ofNullable(userRepository.update(userUpdate));
    }

    @Override
    public Optional<User> findByUsername(final String username) {
        return Optional.ofNullable(userRepository.findByUsername(username));
    }

    @Override
    public List<User> getUserListByPermission(final String permission){
        return userRepository.getUserListByPermission(permission);
    }

    @Override
    public boolean userHasPermissionsOnPage(final User user, final String pageName) {

        boolean hasPermissions = false;
        if ("Page 1".equals(pageName)) {
            hasPermissions = user.getRoles().contains(Constants.PAGE_1);
        } else if ("Page 2".equals(pageName)) {
            hasPermissions = user.getRoles().contains(Constants.PAGE_2);
        } else if ("Page 3".equals(pageName)) {
            hasPermissions = user.getRoles().contains(Constants.PAGE_3);
        }
        return hasPermissions;
    }
}
