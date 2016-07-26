package com.schibsted.server.handler;

import com.schibsted.domain.user.User;
import com.schibsted.server.exception.UnathorizedException;
import com.schibsted.server.messages.user.UserUpdatedOrCreated;
import com.schibsted.server.messages.user.UserfieldsRequired;
import com.schibsted.server.security.AccessUtils;
import com.schibsted.server.utils.ParamsUtils;
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

/**
 * Created by edu on 24/07/2016.
 */
public class UserHandler implements HttpHandler {

    private IUserService userService;
    private AccessUtils accessUtils;

    public UserHandler() {
        if (userService == null) {
            userService = new UserServiceImpl();
        }
        if (accessUtils == null) {
            accessUtils = new AccessUtils();
        }
    }

    private User buildUserFromRequest(HttpExchange httpExchange) throws IOException {
        final InputStreamReader isr = new InputStreamReader(httpExchange.getRequestBody(), "utf-8");
        final BufferedReader br = new BufferedReader(isr);
        final String query = br.readLine();
        final Map<String, String> queryMap = ParamsUtils.queryToMap(query);

        final String username = queryMap.get("username");
        final String password = queryMap.get("password");
        final String roles = queryMap.get("roles");

        return User.build(username, password, getRoles(roles));
    }

    private boolean validateFields(User user){
       return user.getPassword()!=null && !user.getPassword().isEmpty() &&
               user.getUsername()!=null && !user.getUsername().isEmpty();
    }

    @Override
    public void handle(HttpExchange httpExchange) {

        Optional<User> userMod = null;
        try {
            final String authorization = httpExchange.getRequestHeaders().get("Authorization").toString();
            final Optional<User> access = accessUtils.loginUser(authorization);
            if (!access.isPresent()) {
                httpExchange.sendResponseHeaders(403, 0);
                new UnathorizedException(httpExchange.getResponseBody());
            } else {

                final String methodType = httpExchange.getRequestMethod();

                if ("POST".equals(methodType)) {


                    if (!accessUtils.hasAdminPermissions(access.get())) {
                        httpExchange.sendResponseHeaders(403, 0);
                        new UnathorizedException(httpExchange.getResponseBody());

                    } else {
                        final User user = buildUserFromRequest(httpExchange);

                        if(validateFields(user)){
                            userMod = userService.save(user);
                            httpExchange.sendResponseHeaders(200, 0);
                            new UserUpdatedOrCreated(httpExchange.getResponseBody(), userMod.get());
                        }else{
                            httpExchange.sendResponseHeaders(400, 0);
                            new UserfieldsRequired(httpExchange.getResponseBody());
                        }
                    }

                } else if ("PUT".equals(methodType)) {

                    if (!accessUtils.hasAdminPermissions(access.get())) {
                        httpExchange.sendResponseHeaders(403, 0);
                        new UnathorizedException(httpExchange.getResponseBody());

                    } else {
                        final User user = buildUserFromRequest(httpExchange);
                        userMod = userService.update(user);
                        httpExchange.sendResponseHeaders(200, 0);
                        new UserUpdatedOrCreated(httpExchange.getResponseBody(), userMod.get());
                    }

                } else if ("GET".equals(methodType)) {

                    //userMod =userService.findByUsername(user.getUsername());
                } else {
                    //delete not implemented
                }

            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private LinkedHashSet<String> getRoles(String roles) throws UnsupportedEncodingException {

        String param2After = roles.replace("%2C", ",");

        final List<String> roleList =
                Stream.of(param2After.split(","))
                        .collect(Collectors.toList());

        final LinkedHashSet<String> rolesSet = new LinkedHashSet<>();
        roleList.stream().forEach(role -> rolesSet.add(role));
        return rolesSet;
    }
}
