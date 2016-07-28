package com.schibsted.api.user;

import com.google.gson.JsonObject;
import com.schibsted.api.base.BaseTest;

import java.io.IOException;


public abstract class BaseUserTest extends BaseTest {

    protected static final String USERNAME_ADMIN = "admin";
    protected static final String PASSWORD_ADMIN = "admin";

    protected static final String NEW_USERNAME_ADMIN = "admin1234";
    protected static final String NEW_PASSWORD_ADMIN = "admin1234";

    protected String login() throws IOException {
        JsonObject response = getJsonResponsePostForLogin("http://localhost:15000/login", USERNAME_ADMIN, PASSWORD_ADMIN);
        return response.get("auth").getAsString();
    }

}
