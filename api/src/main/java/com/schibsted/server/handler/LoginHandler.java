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

/*
 {
            response.addHeader("Access-Control-Allow-Origin", origin);
        }
        response.setHeader("Access-Control-Allow-Methods", "POST, GET, PUT, OPTIONS, DELETE");
        response.setHeader("Access-Control-Max-Age", "3600");
        response.setHeader("Access-Control-Allow-Headers", "x-uw-act-as, Origin, X-Requested-With, Content-Type, Accept, " + tokenHeader);
        chain.doFilter(req, res);
 */
        Headers headers = httpExchange.getResponseHeaders();
        headers.add("Access-Control-Allow-Headers", "x-uw-act-as, Origin, X-Requested-With, Content-Type, Accept, Authorization");
        headers.add("Access-Control-Allow-Methods", "GET,POST,PUT,OPTIONS");
        headers.add("Access-Control-Allow-Origin", "*");


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

            headers.add("Authorization", "Basic " + encodeUserLogin(username, password));
            httpExchange.sendResponseHeaders(Constants.OPERATION_OK_CODE, 0);
            new UserMessage(httpExchange.getResponseBody(),
                    LoginMessage.build("Basic " + encodeUserLogin(username, password)));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
