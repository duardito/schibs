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
public class SecurityUtils {

    private IUserService userService;

    public SecurityUtils() {
        if(userService == null){
            userService = new UserServiceImpl();
        }
    }

    public Optional<User> loginUser(final String authorization) {

        final String[] values = getDecryptedCreadentials(authorization);
        final String usernameLogin = values[0];
        final String passwdLogin = values[1];
        return userService.loadUserByUsernameAndPassword(usernameLogin, passwdLogin);
    }

    private String[] getDecryptedCreadentials(String authorization) {
        final String base64Credentials = authorization.substring(("Basic".length() + 1), authorization.length() - 1);
        final String credentials = new String(Base64.getDecoder().decode(base64Credentials.trim()),
                Charset.forName("UTF-8"));
        return credentials.split(":", 2);
    }

    public boolean hasAdminPermissions(User user) {
        return user.getRoles().contains(Constants.ADMIN);
    }
}
