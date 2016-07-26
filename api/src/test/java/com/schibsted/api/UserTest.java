package com.schibsted.api;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.schibsted.common.Constants;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by edu on 24/07/2016.
 */
public class UserTest extends BaseTest {

    private static final String NEW_USERNAME_ADMIN = "admin1234";
    private static final String NEW_PASSWORD_ADMIN = "admin1234";

    private static final String USERNAME_ADMIN = "admin";
    private static final String PASSWORD_ADMIN = "admin";

    @Test
    public void updateUserFailingloginNO_OK() throws IOException {

        final String modPassword = "admin1234";
        final String usernameInit = "edu";

        Map<String, String> map = new HashMap<>();
        map.put("username", usernameInit);
        map.put("password", modPassword);
        map.put("roles", "PAGE_1,PAGE_2");
        JsonObject response = getJsonResponsePut("http://localhost:15000/user", map, USERNAME_ADMIN, "1234");
        JsonElement code = response.get("code");
        JsonElement message = response.get("message");

        Assert.assertEquals(code.getAsInt(), Constants.UNATHORIZED_CODE);
        Assert.assertEquals(message.getAsString(), Constants.UNATHORIZED);
    }

    @Test
    public void getUserOK() throws IOException {
        JsonObject response = getJsonResponseGet("http://localhost:15000/user/edu", USERNAME_ADMIN, PASSWORD_ADMIN);

        JsonElement username = response.get("username");
        JsonElement password = response.get("password");

        Assert.assertEquals(username.getAsString(), "edu");
        Assert.assertNull(password);
    }

    @Test
    public void getUserBadRequestNO_OK() throws IOException {
        JsonObject response = getJsonResponseGet("http://localhost:15000/user", USERNAME_ADMIN, PASSWORD_ADMIN);
        JsonElement code = response.get("code");
        JsonElement message = response.get("message");

        Assert.assertEquals(code.getAsInt(), Constants.BAD_REQUEST_CODE);
        Assert.assertEquals(message.getAsString(), Constants.BAD_REQUEST);
    }


    @Test
    public void addUserFailingloginNO_OK() throws IOException {

        Map<String, String> map = new HashMap<>();
        map.put("username", NEW_USERNAME_ADMIN);
        map.put("password", NEW_PASSWORD_ADMIN);
        map.put("roles", "PAGE_1,PAGE_2");
        JsonObject response = getJsonResponsePost("http://localhost:15000/user", map, USERNAME_ADMIN, "1234");
        JsonElement code = response.get("code");
        JsonElement message = response.get("message");

        Assert.assertEquals(code.getAsInt(), Constants.UNATHORIZED_CODE);
        Assert.assertEquals(message.getAsString(), Constants.UNATHORIZED);
    }

    @Test
    public void addUserWithoutPermissionsNO_OK() throws IOException {

        Map<String, String> map = new HashMap<>();
        map.put("username", NEW_USERNAME_ADMIN);
        map.put("password", NEW_PASSWORD_ADMIN);
        map.put("roles", "PAGE_1,PAGE_2");
        JsonObject response = getJsonResponsePost("http://localhost:15000/user", map, "edu", "12345");
        JsonElement code = response.get("code");
        JsonElement message = response.get("message");

        Assert.assertEquals(code.getAsInt(), Constants.UNATHORIZED_CODE);
        Assert.assertEquals(message.getAsString(), Constants.UNATHORIZED);
    }

    @Test
    public void addUserOK() throws IOException {

        Map<String, String> map = new HashMap<>();
        map.put("username", NEW_USERNAME_ADMIN);
        map.put("password", NEW_PASSWORD_ADMIN);
        map.put("roles", "PAGE_1,PAGE_2");
        JsonObject response = getJsonResponsePost("http://localhost:15000/user", map, USERNAME_ADMIN, PASSWORD_ADMIN);
        JsonElement username = response.get("username");
        JsonElement password = response.get("password");

        Assert.assertEquals(username.getAsString(), NEW_USERNAME_ADMIN);
        Assert.assertEquals(password.getAsString(), NEW_PASSWORD_ADMIN);
    }

    @Test
    public void updateUserOK() throws IOException {

        final String modPassword = "admin1234";
        final String usernameInit = "edu";

        Map<String, String> map = new HashMap<>();
        map.put("username", usernameInit);
        map.put("password", modPassword);
        map.put("roles", "PAGE_1,PAGE_2");
        JsonObject response = getJsonResponsePut("http://localhost:15000/user", map, USERNAME_ADMIN, PASSWORD_ADMIN);
        JsonElement username = response.get("username");
        JsonElement password = response.get("password");

        Assert.assertEquals(username.getAsString(), usernameInit);
        Assert.assertEquals(password.getAsString(), modPassword);
    }


    @Test
    public void addUserBadRequestNO_OK() throws IOException {

        final String modPassword = "admin1234";
        final String usernameInit = "edu";

        Map<String, String> map = new HashMap<>();
        map.put("username", usernameInit);
        // map.put("password", modPassword);
        map.put("roles", "PAGE_1,PAGE_2");
        JsonObject response = getJsonResponsePost("http://localhost:15000/user", map, USERNAME_ADMIN, PASSWORD_ADMIN);

        JsonElement code = response.get("code");
        JsonElement message = response.get("message");

        Assert.assertEquals(code.getAsInt(), Constants.BAD_REQUEST_CODE);
        Assert.assertEquals(message.getAsString(), Constants.FIELDS_REQUIRED);
    }
}
