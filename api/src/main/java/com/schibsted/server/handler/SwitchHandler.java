package com.schibsted.server.handler;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.net.URI;

public class SwitchHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        final URI subUri = httpExchange.getRequestURI();
        final String subPath = subUri.getPath();

        //LOGIN OPERATION
        if(subPath.endsWith("login")){
            new LoginHandler().handle(httpExchange);
        }else if(subPath.contains("user")){
            new UserHandler().handle(httpExchange);
        }else if(subPath.contains("Page")){
            new PageHandler().handle(httpExchange);
        }else if(subPath.contains("logout")){
            new LogoutHandler().handle(httpExchange);
        }
    }
}
