package com.schibsted.server.handler;

import com.schibsted.common.Constants;
import com.schibsted.domain.user.User;
import com.schibsted.server.beans.UserCompleteResponse;
import com.schibsted.server.beans.UserReadResponse;
import com.schibsted.server.exception.*;
import com.schibsted.server.messages.user.UserUpdatedOrCreated;
import com.schibsted.server.messages.user.UserfieldsRequired;
import com.schibsted.server.security.SecurityUtils;
import com.schibsted.server.utils.Utils;
import com.schibsted.service.IUserService;
import com.schibsted.service.UserServiceImpl;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class UserHandler implements HttpHandler {

    private IUserService userService;
    private SecurityUtils accessUtils;

    public UserHandler() {
        if (userService == null) {
            userService = new UserServiceImpl();
        }
        if (accessUtils == null) {
            accessUtils = new SecurityUtils();
        }
    }

    private User buildUserFromRequest(final HttpExchange httpExchange) throws IOException {
        final InputStreamReader isr = new InputStreamReader(httpExchange.getRequestBody(), "utf-8");
        final BufferedReader br = new BufferedReader(isr);
        final String query = br.readLine();
        final Map<String, String> queryMap = Utils.queryToMap(query);

        final String username = queryMap.get("username");
        final String password = queryMap.get("password");
        final String roles = queryMap.get("roles");

        return User.build(username, password, getRoles(roles));
    }

    private boolean validateFields(final User user) {
        return user.getPassword() != null && !user.getPassword().isEmpty() &&
                user.getUsername() != null && !user.getUsername().isEmpty();
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

        } catch (Exception e) {

        }
    }

    private Optional<User> getUserAuthorization(HttpExchange httpExchange, String authorization) throws Exception {
        final Optional<User> access = accessUtils.loginUser(authorization);
        if (!access.isPresent()) {
            httpExchange.sendResponseHeaders(Constants.UNATHORIZED_CODE, 0);
            throw new UnathorizedException(httpExchange.getResponseBody());
        }
        return access;
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
        new UserUpdatedOrCreated(httpExchange.getResponseBody(), new UserReadResponse(userFromBody.get()));

    }


    private void executePut(HttpExchange httpExchange, Optional<User> access) throws Exception {

        hashPermissions(httpExchange, access);

        final User userFromRequest = buildUserFromRequest(httpExchange);

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
            new UserUpdatedOrCreated(httpExchange.getResponseBody(),
                    new UserCompleteResponse(userMod.get()));
        }
    }

    private void checkUserAlreadyExists(final HttpExchange httpExchange,final String username) throws Exception {

        final Optional<User> userFromBody = userService.findByUsername(username);
        if (userFromBody.isPresent()) {
            httpExchange.sendResponseHeaders(Constants.CONFLICT_CODE, 0);
            throw new UserAlreadyExistsException(httpExchange.getResponseBody());
        }
    }

    private Optional<User> checkUserNotExists(final HttpExchange httpExchange,final String username) throws Exception {

        final Optional<User> userFromBody = userService.findByUsername(username);
        if (!userFromBody.isPresent()) {
            httpExchange.sendResponseHeaders(Constants.CONFLICT_CODE, 0);
            throw new UserNotFoundException(httpExchange.getResponseBody());
        }
        return userFromBody;
    }

    private void hashPermissions(final HttpExchange httpExchange,final  Optional<User> access) throws Exception {
        if (!accessUtils.hasAdminPermissions(access.get())) {
            httpExchange.sendResponseHeaders(Constants.UNATHORIZED_CODE, 0);
            throw new UnathorizedException(httpExchange.getResponseBody());
        }
    }

    private void executePost(final HttpExchange httpExchange,final  Optional<User> access) throws Exception {

        hashPermissions(httpExchange, access);

        final User user = buildUserFromRequest(httpExchange);
        if (!validateFields(user)) {
            httpExchange.sendResponseHeaders(Constants.BAD_REQUEST_CODE, 0);
            throw new UserfieldsRequired(httpExchange.getResponseBody());
        }

        checkUserAlreadyExists(httpExchange, user.getUsername());

        final Optional<User> userMod = userService.save(user);
        if (userMod.isPresent()) {
            httpExchange.sendResponseHeaders(Constants.OPERATION_OK_CODE, 0);
            new UserUpdatedOrCreated(httpExchange.getResponseBody(),
                    new UserCompleteResponse(userMod.get()));
        }

    }

    private String replacedCharacters(final String roles){
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
