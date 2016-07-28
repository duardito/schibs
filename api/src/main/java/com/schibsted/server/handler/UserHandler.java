package com.schibsted.server.handler;

import com.schibsted.common.Constants;
import com.schibsted.domain.user.User;
import com.schibsted.domain.user.UserDelay;
import com.schibsted.server.beans.user.UserCompleteResponse;
import com.schibsted.server.beans.user.UserReadResponse;
import com.schibsted.server.exception.BadRequestException;
import com.schibsted.server.exception.UpdateNotAllowedException;
import com.schibsted.server.messages.user.UserMessage;
import com.schibsted.server.messages.user.UserfieldsRequired;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class UserHandler extends PermissionsHandler implements HttpHandler {

    private User buildUserFromRequest(Map<String, String> queryMap) throws IOException {

        final String username = queryMap.get("username");
        final String password = queryMap.get("password");
        final String roles = queryMap.get("roles");

        return User.build(username, password, getRoles(roles));
    }

    private boolean validateFields( Map<String, String> queryMap) throws IOException {

        boolean valid = true;

        if (!queryMap.containsKey("username") || !queryMap.containsKey("password")) {
            valid = false;
        }else{
            final String username = queryMap.get("username");
            final String password = queryMap.get("password");
            if( (username == null || username.isEmpty()) && ( password == null || password.isEmpty() ) ){
                valid = false;
            }
        }

        return valid;
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
            resetDelayTime(option);

        } catch (Exception e) {

        }
    }


    private void executeGet(HttpExchange httpExchange) throws Exception {

        final String path = httpExchange.getRequestURI().getPath();
        final String[] pathVariable = path.split("/user/");
        if (pathVariable.length < 2) {
            httpExchange.sendResponseHeaders(Constants.BAD_REQUEST_CODE, 0);
            throw new BadRequestException(httpExchange.getResponseBody());
        }

        final String username = pathVariable[1];
        final Optional<User> userFromBody = checkUserNotExists(httpExchange, username);
        httpExchange.sendResponseHeaders(Constants.OPERATION_OK_CODE, 0);
        new UserMessage(httpExchange.getResponseBody(), new UserReadResponse(userFromBody.get()));

    }


    private void executePut(HttpExchange httpExchange, Optional<User> access) throws Exception {

        final Map<String, String> queryMap = getParamsMap(httpExchange);

        hashPermissions(httpExchange, access);

        final User userFromRequest = buildUserFromRequest(queryMap);

        checkUserNotExists(httpExchange, userFromRequest.getUsername());

        final List<User> userList = userService.getUserListByPermission(Constants.ADMIN);
        if (userList.size() == 1) {
            final User admin = userList.get(0);
            if (admin.getUsername().equals(userFromRequest.getUsername())) {
                if (!userFromRequest.getRoles().contains(Constants.ADMIN)) {
                    httpExchange.sendResponseHeaders(Constants.CONFLICT_CODE, 0);
                    throw new UpdateNotAllowedException(httpExchange.getResponseBody());
                }
            }
        }

        final Optional<User> userMod = userService.update(userFromRequest);
        if (userMod.isPresent()) {
            httpExchange.sendResponseHeaders(Constants.OPERATION_OK_CODE, 0);
            new UserMessage(httpExchange.getResponseBody(),
                    new UserCompleteResponse(userMod.get()));
        }
    }


    private void executePost(final HttpExchange httpExchange, final Optional<User> access) throws Exception {

        final Map<String, String> queryMap = getParamsMap(httpExchange);

        validFieldsPost(httpExchange,queryMap);

        final User user = buildUserFromRequest(queryMap);

        hashPermissions(httpExchange, access);

        checkUserAlreadyExists(httpExchange, user.getUsername());

        final Optional<User> userMod = userService.save(user);
        if (userMod.isPresent()) {
            httpExchange.sendResponseHeaders(Constants.OPERATION_OK_CODE, 0);
            new UserMessage(httpExchange.getResponseBody(),
                    new UserCompleteResponse(userMod.get()));
        }

    }

    private void validFieldsPost(HttpExchange httpExchange,Map<String, String> queryMap) throws Exception {
        if (!validateFields( queryMap)) {
            httpExchange.sendResponseHeaders(Constants.BAD_REQUEST_CODE, 0);
            throw new UserfieldsRequired(httpExchange.getResponseBody());
        }
    }

    private String replacedCharacters(final String roles) {

        return roles.replace("%2C", ",");
    }

    private LinkedHashSet<String> getRoles(final String roles) throws UnsupportedEncodingException {

        final String replacedWhiteSpaces = replacedCharacters(roles);

        final List<String> roleList =
                Stream.of(replacedWhiteSpaces.split(","))
                        .collect(Collectors.toList());

        final LinkedHashSet<String> rolesSet = new LinkedHashSet<>();
        roleList.stream().forEach(role -> rolesSet.add(role));
        return rolesSet;
    }
}
