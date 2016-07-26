package com.schibsted.api;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by edu on 24/07/2016.
 */
public class UserTest extends BaseTest{

    private static final String NEW_USERNAME_ADMIN ="admin1234";
    private static final String NEW_PASSWORD_ADMIN ="admin1234";

    private static final String USERNAME_ADMIN ="admin";
    private static final String PASSWORD_ADMIN ="admin";

    @Test
    public void addUserOK() throws IOException {

        Map<String, String> map= new HashMap<>();
        map.put("username", NEW_USERNAME_ADMIN);
        map.put("password", NEW_PASSWORD_ADMIN);
        map.put("roles", "PAGE_1,PAGE_2");
        JsonObject response = getJsonResponsePost("http://localhost:15000/user", map, "admin", "admin");
        JsonElement username = response.get("username");
        JsonElement password = response.get("password");

        Assert.assertEquals(username.getAsString(), NEW_USERNAME_ADMIN);
        Assert.assertEquals(password.getAsString(), NEW_PASSWORD_ADMIN);
    }

    @Test
    public void updateUserOK() throws IOException {

        final String modPassword="admin1234";

        Map<String, String> map= new HashMap<>();
        map.put("username", USERNAME_ADMIN);
        map.put("password", modPassword);
        map.put("roles", "PAGE_1,PAGE_2");
        JsonObject response = getJsonResponsePut("http://localhost:15000/user", map, "admin", "admin");
        JsonElement username = response.get("username");
        JsonElement password = response.get("password");

        Assert.assertEquals(username.getAsString(), USERNAME_ADMIN);
        Assert.assertEquals(password.getAsString(), modPassword);
    }


}
