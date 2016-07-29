package com.schibsted.server.handler;

import com.schibsted.domain.user.User;
import com.schibsted.domain.user.UserDelay;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.util.Optional;

public class LogoutHandler extends PermissionsHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        try {
            final String authorization = httpExchange.getRequestHeaders().get("Authorization").toString();
            final Optional<User> access = getUserAuthorization(httpExchange, authorization);
            UserDelay delay = UserDelay.build(access.get());
            Optional<UserDelay> userToRemove = Optional.of(delay);
            userDelayService.forceDelayTimeToRemoveUser(userToRemove);

        } catch (Exception e) {

        }
    }
}
