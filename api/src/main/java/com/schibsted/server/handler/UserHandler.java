package com.schibsted.server.handler;

import com.schibsted.domain.role.Role;
import com.schibsted.domain.user.User;
import com.schibsted.server.exception.MessageWrapper;
import com.schibsted.server.exception.OutputMessage;
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

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {

        OutputMessage outputMessage = null;
        Optional <User> userMod = null;
        try {
            final String authorization = httpExchange.getRequestHeaders().get("Authorization").toString();
            final String access = accessUtils.loginUser(authorization);
            if ("403".equals(access)) {
                httpExchange.sendResponseHeaders(403, 0);

                outputMessage = new OutputMessage();
                outputMessage.setCode("403");
                outputMessage.setMessage("unathorized access");

            } else {
                final InputStreamReader isr = new InputStreamReader(httpExchange.getRequestBody(), "utf-8");
                final BufferedReader br = new BufferedReader(isr);
                final String query = br.readLine();
                final Map<String, String> queryMap = ParamsUtils.queryToMap(query);

                final String username = queryMap.get("username");
                final String password = queryMap.get("password");
                final String roles = queryMap.get("roles");

                final User user = User.build(username, password, getRoles(roles));


                final String methodType = httpExchange.getRequestMethod();

                if ("POST".equals(methodType)) {
                    userMod = userService.save(user);
                } else if ("PUT".equals(methodType)) {
                    userMod =userService.update(user);
                } else if ("GET".equals(methodType)) {
                    userMod =userService.findByUsername(user.getUsername());
                } else {
                    //delete not implemented
                }
                httpExchange.sendResponseHeaders(200, 0);

                outputMessage = new OutputMessage();
                outputMessage.setCode("200");
                outputMessage.setMessage("succesful operation");
            }
        } finally {
            MessageWrapper.build(httpExchange.getResponseBody(), userMod);
        }
    }


    private LinkedHashSet<Role> getRoles(String roles) throws UnsupportedEncodingException {

        String param2After = roles.replace("%2C", ",");

        final List<String> roleList =
                Stream.of(param2After.split(","))
                        .collect(Collectors.toList());

        final LinkedHashSet<Role> rolesSet = new LinkedHashSet<>();
        roleList.stream().forEach(role -> rolesSet.add(Role.build(role)));
        return rolesSet;
    }
}
