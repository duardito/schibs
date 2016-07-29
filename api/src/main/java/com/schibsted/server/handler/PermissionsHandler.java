package com.schibsted.server.handler;

import com.schibsted.common.Constants;
import com.schibsted.domain.user.User;
import com.schibsted.domain.user.UserDelay;
import com.schibsted.server.exception.*;
import com.schibsted.server.messages.user.UserfieldsRequired;
import com.schibsted.server.utils.Utils;
import com.schibsted.service.IUserDelayService;
import com.schibsted.service.IUserService;
import com.schibsted.service.UserDelayServiceImpl;
import com.schibsted.service.UserServiceImpl;
import com.sun.net.httpserver.HttpExchange;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public abstract class PermissionsHandler {


    protected IUserService userService;
    protected IUserDelayService userDelayService;


    public PermissionsHandler() {
        if (userService == null) {
            userService = new UserServiceImpl();
        }
        if (userDelayService == null) {
            userDelayService = new UserDelayServiceImpl();
        }
    }

    protected void checkUserExists(HttpExchange httpExchange, Optional<User> loggedUser) throws Exception {
        if (!loggedUser.isPresent()) {
            httpExchange.sendResponseHeaders(Constants.CONFLICT_CODE, 0);
            throw new UserNotFoundException(httpExchange.getResponseBody());
        }
    }

    protected void checkHasPathVariable(HttpExchange httpExchange, String[] pathVariable) throws Exception {
        if (pathVariable.length < 2) {
            httpExchange.sendResponseHeaders(Constants.BAD_REQUEST_CODE, 0);
            throw new BadRequestException(httpExchange.getResponseBody());
        }
    }

    protected void validFieldsPost(HttpExchange httpExchange, Map<String, String> queryMap) throws Exception {
        if (!validateFields(queryMap)) {
            httpExchange.sendResponseHeaders(Constants.BAD_REQUEST_CODE, 0);
            throw new UserfieldsRequired(httpExchange.getResponseBody());
        }
    }

    private boolean validateFields(Map<String, String> queryMap) throws IOException {

        boolean valid = true;

        if (!queryMap.containsKey("username") || !queryMap.containsKey("password")) {
            valid = false;
        } else {
            final String username = queryMap.get("username");
            final String password = queryMap.get("password");
            if ((username == null || username.isEmpty()) && (password == null || password.isEmpty())) {
                valid = false;
            }
        }
        return valid;
    }

    protected void validateRoles(HttpExchange httpExchange, final String roles) throws Exception {
        if (!validateRoles(roles)) {
            httpExchange.sendResponseHeaders(Constants.BAD_REQUEST_CODE, 0);
            throw new BadRequestException(httpExchange.getResponseBody());
        }
    }

    private boolean validateRoles(final String roles) throws UnsupportedEncodingException {
        final LinkedHashSet<String> rolesSet = new LinkedHashSet<>();
        rolesSet.add(Constants.PAGE_1);
        rolesSet.add(Constants.PAGE_2);
        rolesSet.add(Constants.PAGE_3);
        rolesSet.add(Constants.ADMIN);

        LinkedHashSet<String> rolesToCheck = getRoles(roles);
        return rolesSet.stream().filter(role -> !rolesToCheck.contains(role)).findAny().isPresent();
    }

    private String replacedCharacters(final String roles) {
        return roles.replace("%2C", ",");
    }

    protected LinkedHashSet<String> getRoles(final String roles) throws UnsupportedEncodingException {

        final String replacedWhiteSpaces = replacedCharacters(roles);

        final List<String> roleList =
                Stream.of(replacedWhiteSpaces.split(","))
                        .collect(Collectors.toList());

        final LinkedHashSet<String> rolesSet = new LinkedHashSet<>();
        roleList.stream().forEach(role -> rolesSet.add(role));
        return rolesSet;
    }

    protected void checkOnlyOneAdmin(HttpExchange httpExchange, User userFromRequest) throws Exception {
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
    }

    protected void RequestHasAuthorization(HttpExchange httpExchange, Map<String, String> params) throws Exception {
        if (!params.containsKey("Authorization")) {
            httpExchange.sendResponseHeaders(Constants.NOT_LOGGED_IN_CODE, 0);
            throw new UnathorizedException(httpExchange.getResponseBody());
        }
    }

    private String getUsernameFromAuthorization(final String[] values) {
        return values[0];
    }

    private String getPasswordFromAuthorization(final String[] values) {
        return values[1];
    }

    protected String[] getCredentials(final String authorization) {
        return getDecryptedCreadentials(authorization);
    }

    protected User loginUser(final String authorization) {

        final String[] values = getCredentials(authorization);
        final String usernameLogin = getUsernameFromAuthorization(values);
        final String passwdLogin = getPasswordFromAuthorization(values);
        return userService.loadUserByUsernameAndPassword(usernameLogin, passwdLogin);
    }

    private String[] getDecryptedCreadentials(String authorization) {
        int lent = 0;
        if (authorization.endsWith("]")) {
            lent = authorization.length() - 1;
        } else {
            lent = authorization.length();
        }
        final String base64Credentials = authorization.substring(("Basic".length() + 1), lent);
        final String credentials = new String(Base64.getDecoder().decode(base64Credentials.trim()),
                Charset.forName("UTF-8"));
        return credentials.split(":", 2);
    }

    protected boolean hasAdminPermissions(User user) {
        return user.getRoles().contains(Constants.ADMIN);
    }

    protected Map<String, String> getParamsMap(HttpExchange httpExchange) throws IOException {
        final InputStreamReader isr = new InputStreamReader(httpExchange.getRequestBody(), "utf-8");
        final BufferedReader br = new BufferedReader(isr);
        final String query = br.readLine();
        return Utils.queryToMap(query);
    }

    protected Optional<User> getUserAuthorization(HttpExchange httpExchange, String authorization) throws Exception {

        final Optional<User> loggedUser = Optional.of(loginUser(authorization));

        checkUserExists(httpExchange, loggedUser);
        userDelayService.usersInDelay(loggedUser);

        final Optional<UserDelay> userInDelay = userDelayService.getUserDelay(loggedUser.get().getUsername());
        if (!userInDelay.isPresent()) {
            httpExchange.sendResponseHeaders(Constants.NOT_LOGGED_IN_CODE, 0);
            throw new UnathorizedException(httpExchange.getResponseBody());
        }

        if (!loggedUser.isPresent()) {
            httpExchange.sendResponseHeaders(Constants.UNATHORIZED_CODE, 0);
            throw new UnathorizedException(httpExchange.getResponseBody());
        }
        return loggedUser;
    }

    protected void checkAuthorization(HttpExchange httpExchange, String authorization) throws Exception {
        if (authorization == null) {
            httpExchange.sendResponseHeaders(Constants.NOT_LOGGED_IN_CODE, 0);
            throw new UnathorizedException(httpExchange.getResponseBody());
        }
    }

    protected void checkUserAlreadyExists(final HttpExchange httpExchange, final String username) throws Exception {
        final Optional<User> userFromBody = userService.findByUsername(username);
        if (userFromBody.isPresent()) {
            httpExchange.sendResponseHeaders(Constants.CONFLICT_CODE, 0);
            throw new UserAlreadyExistsException(httpExchange.getResponseBody());
        }
    }

    protected Optional<User> checkUserNotExists(final HttpExchange httpExchange, final String username) throws Exception {
        final Optional<User> userFromBody = userService.findByUsername(username);
        if (!userFromBody.isPresent()) {
            httpExchange.sendResponseHeaders(Constants.CONFLICT_CODE, 0);
            throw new UserNotFoundException(httpExchange.getResponseBody());
        }
        return userFromBody;
    }

    protected void userHashAdminPermissions(final HttpExchange httpExchange, final Optional<User> access) throws Exception {
        if (!hasAdminPermissions(access.get())) {
            httpExchange.sendResponseHeaders(Constants.UNATHORIZED_CODE, 0);
            throw new UnathorizedException(httpExchange.getResponseBody());
        }
    }

    protected void UserHasPermissionsOnPage(HttpExchange httpExchange, Optional<User> access, String path) throws Exception {
        if (!userService.userHasPermissionsOnPage(access.get(), path)) {
            httpExchange.sendResponseHeaders(Constants.UNATHORIZED_CODE, 0);
            throw new UserHasNotPermissionsException(httpExchange.getResponseBody());
        }
    }
}
