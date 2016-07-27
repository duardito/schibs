package com.schibsted.server.handler;

import com.schibsted.domain.user.User;
import com.schibsted.server.utils.Utils;
import com.schibsted.service.IUserService;
import com.schibsted.service.UserServiceImpl;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.util.Map;
import java.util.Optional;

public class LoginHandler implements HttpHandler {


    private IUserService userService;

    public LoginHandler() {
        if(userService == null){
            userService = new UserServiceImpl();
        }
    }

    public void handle(HttpExchange httpExchange) throws IOException {

        Headers responseHeaders = httpExchange.getResponseHeaders();
        responseHeaders.set("Content-Type","application/x-www-form-urlencoded");
        //responseHeaders.set("Content-Type","application/json");

        InputStreamReader isr =  new InputStreamReader(httpExchange.getRequestBody(),"utf-8");
        BufferedReader br = new BufferedReader(isr);
        String query = br.readLine();
        final Map<String, String> queryMap = Utils.queryToMap(query);

        final String username = queryMap.get("username");
        final String password = queryMap.get("password");

       Optional <User> loggedUser = userService.loadUserByUsernameAndPassword(username, password);

        httpExchange.sendResponseHeaders(200, 0);

        ObjectOutputStream objOut = new     ObjectOutputStream(httpExchange.getResponseBody());
        objOut.writeObject(loggedUser);
        objOut.close();
    }
}
