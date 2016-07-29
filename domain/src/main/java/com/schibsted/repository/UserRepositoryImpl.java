package com.schibsted.repository;

import com.schibsted.common.Constants;
import com.schibsted.domain.user.User;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class UserRepositoryImpl implements IUserRepository {

    private static List<User> userList = new ArrayList<User>();
    static {
        initUsers();
    }

    private static void initUsers() {


        LinkedHashSet<String> role = new LinkedHashSet<String>();
        role.add(Constants.PAGE_1);
        User userPage1 = User.build("edu", "12345", role);

        userList.add(userPage1);

        role = new LinkedHashSet<String>();
        role.add(Constants.PAGE_2);
        User userPage2 = User.build("toni", "5678", role);

        userList.add(userPage2);

        role = new LinkedHashSet<String>();
        role.add(Constants.PAGE_3);
        role.add(Constants.PAGE_2);
        User userPage3 = User.build("juan", "7788", role);

        userList.add(userPage3);

        role = new LinkedHashSet<String>();
        role.add(Constants.ADMIN);
        User userAdmin = User.build("admin", "admin", role);

        userList.add(userAdmin);
    }

    @Override
    public User loadUserByUsernameAndPassword(final String username, final String password) {
        return userList.stream()
                .filter(user -> user.getUsername().equals(username) && user.getPassword().equals(password))
                .findAny()
                .orElse(null);
    }

    @Override
    public User save(final User user) {
        userList.add(user);
        return user;
    }

    @Override
    public User update(final User userUpdate) {
        final User toUpdate = userList.stream()
                .filter(user -> user.getUsername().equals(userUpdate.getUsername()))
                .findAny()
                .orElse(null);
        final Optional<User> optionalUser = Optional.ofNullable(toUpdate);
        if (optionalUser.isPresent()) {
            userList.remove(toUpdate);
            userList.add(userUpdate);
        }
        return userUpdate;
    }

    @Override
    public User findByUsername(final String username) {
        return userList.stream()
                .filter(user -> user.getUsername().equals(username))
                .findAny()
                .orElse(null);
    }

    @Override
    public List <User> getUserListByPermission(final String permission){
        return userList.stream()
                .filter(user -> user.getRoles().contains(permission))
                .collect(Collectors.toList());
    }

}
