package com.schibsted.server.handler;

import com.schibsted.domain.user.User;
import com.schibsted.server.utils.ParamsUtils;
import com.schibsted.service.IUserService;
import com.schibsted.service.UserServiceImpl;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.util.Map;

/**
 * Created by edu on 23/07/2016.
 */
public class InitHandler implements HttpHandler {


    private IUserService userService;

    public InitHandler() {
        if(userService == null){
            userService = new UserServiceImpl();
        }
    }

    public void handle(HttpExchange httpExchange) throws IOException {

        Map<String, String> queryMap = ParamsUtils.queryToMap(httpExchange);

        String username = queryMap.get("username");
        String password = queryMap.get("password");

        User loggedUser = userService.loadUserByUsernameAndPassword(username, password);


    }
}
