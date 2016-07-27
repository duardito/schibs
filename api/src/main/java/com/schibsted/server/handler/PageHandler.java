package com.schibsted.server.handler;

import com.schibsted.common.Constants;
import com.schibsted.domain.user.User;
import com.schibsted.server.exception.UnathorizedException;
import com.schibsted.server.security.SecurityUtils;
import com.schibsted.service.IUserService;
import com.schibsted.service.UserServiceImpl;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.util.Optional;


public class PageHandler implements HttpHandler {

    private IUserService userService;
    private SecurityUtils accessUtils;

    public PageHandler() {
        if (userService == null) {
            userService = new UserServiceImpl();
        }
        if (accessUtils == null) {
            accessUtils = new SecurityUtils();
        }
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        final String authorization = httpExchange.getRequestHeaders().get("Authorization").toString();
        try {
            //user has not authorization, so it is not logged in
            if (authorization == null) {
                httpExchange.sendResponseHeaders(Constants.NOT_LOGGED_IN_CODE, 0);

                throw new UnathorizedException(httpExchange.getResponseBody());
            }
            final Optional<User> access = accessUtils.loginUser(authorization);
            //authorization header is present
            if (!access.isPresent()) {
                httpExchange.sendResponseHeaders(Constants.UNATHORIZED_CODE, 0);
                throw new UnathorizedException(httpExchange.getResponseBody());

            }
            final String path = httpExchange.getRequestURI().getPath();
            userService.userHasPermissionsOnPage(access.get(), path.substring(1, path.length()));

        } catch (UnathorizedException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}
