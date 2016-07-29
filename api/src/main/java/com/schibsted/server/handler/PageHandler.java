package com.schibsted.server.handler;

import com.schibsted.common.Constants;
import com.schibsted.domain.user.User;
import com.schibsted.domain.user.UserDelay;
import com.schibsted.server.beans.page.PageResponse;
import com.schibsted.server.exception.UnathorizedException;
import com.schibsted.server.messages.page.PageMessageApi;
import com.schibsted.server.utils.Utils;
import com.schibsted.service.IUserService;
import com.schibsted.service.UserServiceImpl;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;


public class PageHandler extends PermissionsHandler implements HttpHandler {

    private IUserService userService;

    public PageHandler() {
        if (userService == null) {
            userService = new UserServiceImpl();
        }
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        try {

            Headers headers = httpExchange.getResponseHeaders();
            headers.add("Access-Control-Allow-Headers", "x-uw-act-as, Origin, X-Requested-With, Content-Type, Accept, Authorization");
            headers.add("Access-Control-Allow-Methods", "GET,OPTIONS");
            headers.add("Access-Control-Allow-Origin", "*");


            final Map<String, String> params = Utils.queryToMap(httpExchange.getRequestURI().getQuery());

            RequestHasAuthorization(httpExchange, params);
            final String authorization = params.get("Authorization");

            checkAuthorization(httpExchange, authorization);

            final Optional<User> access = getUserAuthorization(httpExchange, authorization);

            String path = httpExchange.getRequestURI().getPath();
            path = path.substring(1, path.length());

            UserHasPermissionsOnPage(httpExchange, access, path);

            final Optional<UserDelay> option = Optional.of(UserDelay.build(access.get()));
            userDelayService.resetDelayTime(option);

            httpExchange.sendResponseHeaders(Constants.OPERATION_OK_CODE, 0);
            new PageMessageApi(httpExchange.getResponseBody(), new PageResponse(path, access.get().getUsername()));
        } catch (UnathorizedException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }




}
