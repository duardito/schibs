package com.schibsted.server.security;

import com.schibsted.common.Constants;
import com.schibsted.domain.user.User;
import com.schibsted.service.IUserService;
import com.schibsted.service.UserServiceImpl;

import java.nio.charset.Charset;
import java.util.Base64;
import java.util.Optional;

/**
 * Created by edu on 24/07/2016.
 */
public class AccessUtils {

    private IUserService userService;

    public AccessUtils() {
        userService = new UserServiceImpl();
    }

    public Optional<User> loginUser(final String authorization) {

        final String base64Credentials = authorization.substring(("Basic".length() + 1), authorization.length() - 1);
        final String credentials = new String(Base64.getDecoder().decode(base64Credentials.trim()),
                Charset.forName("UTF-8"));
        final String[] values = credentials.split(":", 2);
        final String usernameLogin = values[0];
        final String passwdLogin = values[1];
        return userService.loadUserByUsernameAndPassword(usernameLogin, passwdLogin);
    }

    public boolean hasAdminPermissions(User user) {
        return user.getRoles().contains(Constants.ADMIN);
    }
}
