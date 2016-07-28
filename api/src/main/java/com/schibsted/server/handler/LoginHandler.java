package com.schibsted.server.handler;

import com.schibsted.common.Constants;
import com.schibsted.domain.user.User;
import com.schibsted.server.exception.UserNotFoundException;
import com.schibsted.server.messages.LoginMessage;
import com.schibsted.server.messages.user.UserMessage;
import com.schibsted.service.IUserService;
import com.schibsted.service.UserServiceImpl;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Map;
import java.util.Optional;

public class LoginHandler extends PermissionsHandler implements HttpHandler {


    private IUserService userService;

    public LoginHandler() {
        if (userService == null) {
            userService = new UserServiceImpl();
        }
    }

    private String encodeUserLogin(final String username, final String password) {
        final String build = username + ":" + password;
        final byte[] message = build.getBytes(StandardCharsets.UTF_8);
        return Base64.getEncoder().encodeToString(message);
    }

    public void handle(HttpExchange httpExchange) throws IOException {

        Headers responseHeaders = httpExchange.getResponseHeaders();
        //responseHeaders.set("Content-Type", "application/x-www-form-urlencoded");
        //responseHeaders.set("Content-Type","application/json");


        try {
            final Map<String, String> queryMap = getParamsMap(httpExchange);
            final String username = queryMap.get("username");
            final String password = queryMap.get("password");

            final User user = userService.loadUserByUsernameAndPassword(username, password);
            final Optional<User> loggedUser = Optional.ofNullable(user);
            if (!loggedUser.isPresent()) {
                httpExchange.sendResponseHeaders(Constants.CONFLICT_CODE, 0);
                throw new UserNotFoundException(httpExchange.getResponseBody());
            }

            usersInDelay(loggedUser);

            httpExchange.sendResponseHeaders(Constants.OPERATION_OK_CODE, 0);
            new UserMessage(httpExchange.getResponseBody(),
                    LoginMessage.build("Basic " + encodeUserLogin(username, password)));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
