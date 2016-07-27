package com.schibsted.server.handler;

import com.schibsted.common.Constants;
import com.schibsted.domain.user.User;
import com.schibsted.server.beans.user.UserCompleteResponse;
import com.schibsted.server.messages.user.UserUpdatedOrCreated;
import com.schibsted.service.IUserService;
import com.schibsted.service.UserServiceImpl;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;

public class LoginHandler extends PermissionsHandler implements HttpHandler {


    private IUserService userService;

    public LoginHandler() {
        if (userService == null) {
            userService = new UserServiceImpl();
        }
    }

    public void handle(HttpExchange httpExchange) throws IOException {

        Headers responseHeaders = httpExchange.getResponseHeaders();
        //responseHeaders.set("Content-Type", "application/x-www-form-urlencoded");
        //responseHeaders.set("Content-Type","application/json");

        final Map<String, String> queryMap = getParamsMap(httpExchange);

        final String username = queryMap.get("username");
        final String password = queryMap.get("password");

        final Optional<User> loggedUser = userService.loadUserByUsernameAndPassword(username, password);

        try {
            checkUserNotExists(httpExchange, username);
            usersInDelay(loggedUser);

            httpExchange.sendResponseHeaders(Constants.OPERATION_OK_CODE, 0);
            new UserUpdatedOrCreated(httpExchange.getResponseBody(),
                    new UserCompleteResponse(loggedUser.get()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }




}
