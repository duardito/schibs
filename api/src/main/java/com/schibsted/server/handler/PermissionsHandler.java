package com.schibsted.server.handler;

import com.schibsted.common.Constants;
import com.schibsted.domain.user.User;
import com.schibsted.domain.user.UserDelay;
import com.schibsted.server.exception.UnathorizedException;
import com.schibsted.server.exception.UserAlreadyExistsException;
import com.schibsted.server.exception.UserHasNotPermissionsException;
import com.schibsted.server.exception.UserNotFoundException;
import com.schibsted.server.utils.Utils;
import com.schibsted.service.IUserService;
import com.schibsted.service.UserServiceImpl;
import com.sun.net.httpserver.HttpExchange;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.Base64;
import java.util.Date;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.DelayQueue;

/**
 * Created by edu on 27/7/16.
 */
public abstract class PermissionsHandler {

    protected IUserService userService;
    protected static DelayQueue<UserDelay> userDelayedList= new DelayQueue<UserDelay>();

    public PermissionsHandler(){
        if (userService == null) {
            userService = new UserServiceImpl();
        }
    }

    private String getUsernameFromAuthorization(final String[] values){
        return values[0];
    }

    private String getPasswordFromAuthorization(final String[] values){
        return values[1];
    }

    public String [] getCredentials(final String authorization){
        return getDecryptedCreadentials(authorization);
    }

    protected Optional<User> loginUser(final String authorization) {

        final String[] values = getCredentials(authorization);
        final String usernameLogin = getUsernameFromAuthorization(values);
        final String passwdLogin = getPasswordFromAuthorization(values);
        return userService.loadUserByUsernameAndPassword(usernameLogin, passwdLogin);
    }

    private String[] getDecryptedCreadentials(String authorization) {
        final String base64Credentials = authorization.substring(("Basic".length() + 1), authorization.length() - 1);
        final String credentials = new String(Base64.getDecoder().decode(base64Credentials.trim()),
                Charset.forName("UTF-8"));
        return credentials.split(":", 2);
    }

    protected boolean hasAdminPermissions(User user) {
        return user.getRoles().contains(Constants.ADMIN);
    }

    static{
        removeUser();
    }

    //remove loged users from session after 5 minutes
    private static void removeUser(){
        Runnable task2 = () -> {
            while(true){
                if(getUserDelayedList() != null && !getUserDelayedList().isEmpty()){
                    final UserDelay peek = getUserDelayedList().peek();
                    if(getUserDelayedList().poll()  != null){
                        System.out.println("User: "+peek.getUser()+" removed from list at: " + new Date());
                    }
                }
            }
        };
        new Thread(task2).start();
    }

    protected void usersInDelay(Optional<User> loggedUser) {
        if (userDelayedList.isEmpty()) {
            userDelayedList.offer(UserDelay.build(loggedUser.get()));
        } else {
            final Optional<UserDelay> delay = getUserDelay(loggedUser.get().getUsername());
            if (!delay.isPresent()) {
                userDelayedList.offer(UserDelay.build(loggedUser.get()));
            } else {
                resetDelayTime(delay);
            }
        }
    }

    private Optional<UserDelay> getUserDelay(final String username) {
        return userDelayedList.stream()
                        .filter(userDelay -> userDelay.getUser().getUsername().equals(username)).findAny();
    }

    protected void resetDelayTime(final Optional<UserDelay> delay) {
        delay.get().setDelayTime(System.currentTimeMillis() + Constants._DELAYTIME);
    }



    public static DelayQueue<UserDelay> getUserDelayedList() {
        return userDelayedList;
    }


    protected Map<String, String> getParamsMap(HttpExchange httpExchange) throws IOException {
        final InputStreamReader isr = new InputStreamReader(httpExchange.getRequestBody(), "utf-8");
        final BufferedReader br = new BufferedReader(isr);
        final String query = br.readLine();
        return Utils.queryToMap(query);
    }

    protected Optional<User> getUserAuthorization(HttpExchange httpExchange, String authorization) throws Exception {

        final String[] values = getCredentials(authorization);
        final String usernameLogin = getUsernameFromAuthorization(values);
        Optional<UserDelay> userInDelay = getUserDelay(usernameLogin);
        if(!userInDelay.isPresent()){
            httpExchange.sendResponseHeaders(Constants.NOT_LOGGED_IN_CODE, 0);
            throw new UnathorizedException(httpExchange.getResponseBody());
        }

        final Optional<User> access = loginUser(authorization);

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
        if (!hasAdminPermissions(access.get())) {
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
