package com.schibsted.server.handler;

import com.schibsted.common.Constants;
import com.schibsted.domain.user.User;
import com.schibsted.server.beans.page.PageResponse;
import com.schibsted.server.exception.UnathorizedException;
import com.schibsted.server.messages.page.PageMessageApi;
import com.schibsted.server.security.SecurityUtils;
import com.schibsted.service.IUserService;
import com.schibsted.service.UserServiceImpl;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.util.Optional;


public class PageHandler extends PermissionsHandler implements HttpHandler {

    private IUserService userService;
    private SecurityUtils accessUtils;

    public PageHandler() {
        if (userService == null) {
            userService = new UserServiceImpl();
        }
        if (accessUtils == null) {
            accessUtils = new SecurityUtils();
        }
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        final String authorization = httpExchange.getRequestHeaders().get("Authorization").toString();
        try {
            //user has not authorization, so it is not logged in
            checkAuthorization(httpExchange, authorization);

            final Optional<User> access = getUserAuthorization(httpExchange, authorization);

            String path = httpExchange.getRequestURI().getPath();
            path = path.substring(1, path.length());

            UserHasPermissionsOnPage(httpExchange, access, path);

            httpExchange.sendResponseHeaders(Constants.OPERATION_OK_CODE, 0);
            new PageMessageApi(httpExchange.getResponseBody(), new PageResponse(path, access.get().getUsername()));
        } catch (UnathorizedException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }


}
