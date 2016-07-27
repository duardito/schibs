package com.schibsted.server.handler;

import com.schibsted.common.Constants;
import com.schibsted.domain.user.User;
import com.schibsted.server.exception.UnathorizedException;
import com.schibsted.server.exception.UserAlreadyExistsException;
import com.schibsted.server.exception.UserHasNotPermissionsException;
import com.schibsted.server.exception.UserNotFoundException;
import com.schibsted.server.security.SecurityUtils;
import com.schibsted.service.IUserService;
import com.schibsted.service.UserServiceImpl;
import com.sun.net.httpserver.HttpExchange;

import java.util.Optional;

/**
 * Created by edu on 27/7/16.
 */
public abstract class PermissionsHandler {

    protected SecurityUtils accessUtils;
    protected IUserService userService;

    public PermissionsHandler(){
        if (accessUtils == null) {
            accessUtils = new SecurityUtils();
        }
        if (userService == null) {
            userService = new UserServiceImpl();
        }
    }

    protected Optional<User> getUserAuthorization(HttpExchange httpExchange, String authorization) throws Exception {
        final Optional<User> access = accessUtils.loginUser(authorization);
        if (!access.isPresent()) {
            httpExchange.sendResponseHeaders(Constants.UNATHORIZED_CODE, 0);
            throw new UnathorizedException(httpExchange.getResponseBody());
        }
        return access;
    }

    protected void checkAuthorization(HttpExchange httpExchange, String authorization) throws Exception {
        if (authorization == null) {
            httpExchange.sendResponseHeaders(Constants.NOT_LOGGED_IN_CODE, 0);
            throw new UnathorizedException(httpExchange.getResponseBody());
        }
    }

    protected void checkUserAlreadyExists(final HttpExchange httpExchange,final String username) throws Exception {

        final Optional<User> userFromBody = userService.findByUsername(username);
        if (userFromBody.isPresent()) {
            httpExchange.sendResponseHeaders(Constants.CONFLICT_CODE, 0);
            throw new UserAlreadyExistsException(httpExchange.getResponseBody());
        }
    }

    protected Optional<User> checkUserNotExists(final HttpExchange httpExchange,final String username) throws Exception {

        final Optional<User> userFromBody = userService.findByUsername(username);
        if (!userFromBody.isPresent()) {
            httpExchange.sendResponseHeaders(Constants.CONFLICT_CODE, 0);
            throw new UserNotFoundException(httpExchange.getResponseBody());
        }
        return userFromBody;
    }

    protected void hashPermissions(final HttpExchange httpExchange,final  Optional<User> access) throws Exception {
        if (!accessUtils.hasAdminPermissions(access.get())) {
            httpExchange.sendResponseHeaders(Constants.UNATHORIZED_CODE, 0);
            throw new UnathorizedException(httpExchange.getResponseBody());
        }
    }

    protected void UserHasPermissionsOnPage(HttpExchange httpExchange, Optional<User> access, String path) throws Exception {
        if (!userService.userHasPermissionsOnPage(access.get(), path ) ) {
            httpExchange.sendResponseHeaders(Constants.UNATHORIZED_CODE, 0);
            throw new UserHasNotPermissionsException(httpExchange.getResponseBody());
        }
    }
}
