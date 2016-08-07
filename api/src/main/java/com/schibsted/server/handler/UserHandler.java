package com.schibsted.server.handler;

import com.schibsted.common.Constants;
import com.schibsted.domain.user.User;
import com.schibsted.domain.user.UserDelay;
import com.schibsted.server.beans.user.UserCompleteResponse;
import com.schibsted.server.beans.user.UserReadResponse;
import com.schibsted.server.messages.user.UserMessage;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;

public class UserHandler extends PermissionsHandler implements HttpHandler {

    private User buildUserFromRequest(Map<String, String> queryMap) throws IOException {

        String username = null;
        String password = null;
        String roles = null;
        if (queryMap.containsKey("username")) {
            username = queryMap.get("username");
        }
        if (queryMap.containsKey("password")) {
            password = queryMap.get("password");
        }
        if (queryMap.containsKey("roles")) {
            roles = queryMap.get("roles");
        }

        return User.build(username, password, getRoles(roles));
    }


    @Override
    public void handle(HttpExchange httpExchange) {

        try {
            final String authorization = httpExchange.getRequestHeaders().get("Authorization").toString();
            final Optional<User> access = getUserAuthorization(httpExchange, authorization);
            final String methodType = httpExchange.getRequestMethod();

            if ("POST".equals(methodType)) {

                executePost(httpExchange, access);

            } else if ("PUT".equals(methodType)) {

                executePut(httpExchange, access);

            } else if ("GET".equals(methodType)) {

                executeGet(httpExchange);

            } else {
                //delete not implemented
            }

            final Optional<UserDelay> option = Optional.of(UserDelay.build(access.get()));
            userDelayService.resetDelayTime(option);

        } catch (Exception e) {

        }
    }

    private void executeGet(HttpExchange httpExchange) throws Exception {

        final String path = httpExchange.getRequestURI().getPath();
        final String[] pathVariable = path.split("/user/");
        checkHasPathVariable(httpExchange, pathVariable);

        final String username = pathVariable[1];
        final Optional<User> userFromBody = checkUserNotExists(httpExchange, username);
        httpExchange.sendResponseHeaders(Constants.OPERATION_OK_CODE, 0);
        new UserMessage(httpExchange.getResponseBody(), new UserReadResponse(userFromBody.get()));
    }

    private void executePut(HttpExchange httpExchange, Optional<User> access) throws Exception {

        final Map<String, String> queryMap = getParamsMap(httpExchange);

        userHashAdminPermissions(httpExchange, access);

        final User userFromRequest = buildUserFromRequest(queryMap);

        checkUserNotExists(httpExchange, userFromRequest.getUsername());

        checkOnlyOneAdmin(httpExchange, userFromRequest);

        final Optional<User> userMod = userService.update(userFromRequest);
        if (userMod.isPresent()) {
            httpExchange.sendResponseHeaders(Constants.OPERATION_OK_CODE, 0);
            new UserMessage(httpExchange.getResponseBody(),
                    new UserCompleteResponse(userMod.get()));
        }
    }

    private void executePost(final HttpExchange httpExchange, final Optional<User> access) throws Exception {

        final Map<String, String> queryMap = getParamsMap(httpExchange);

        validFieldsPost(httpExchange, queryMap);

        final User user = buildUserFromRequest(queryMap);

        userHashAdminPermissions(httpExchange, access);

        checkUserAlreadyExists(httpExchange, user.getUsername());

        final Optional<User> userMod = userService.save(user);
        if (userMod.isPresent()) {
            httpExchange.sendResponseHeaders(Constants.OPERATION_OK_CODE, 0);
            new UserMessage(httpExchange.getResponseBody(),
                    new UserCompleteResponse(userMod.get()));
        }
    }



}
