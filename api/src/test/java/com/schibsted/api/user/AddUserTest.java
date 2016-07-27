package com.schibsted.api.user;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.schibsted.common.Constants;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class AddUserTest extends BaseUserTest{

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
    public void addUserBadRequestNO_OK() throws IOException {

        final String usernameInit = "edu";

        Map<String, String> map = new HashMap<>();
        map.put("username", usernameInit);
        map.put("roles", "PAGE_1,PAGE_2");
        JsonObject response = getJsonResponsePost("http://localhost:15000/user", map, USERNAME_ADMIN, PASSWORD_ADMIN);

        JsonElement code = response.get("code");
        JsonElement message = response.get("message");

        Assert.assertEquals(code.getAsInt(), Constants.BAD_REQUEST_CODE);
        Assert.assertEquals(message.getAsString(), Constants.FIELDS_REQUIRED);
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
    public void addUserAlreadyExistsNO_OK() throws IOException {

        Map<String, String> map = new HashMap<>();
        map.put("username", USERNAME_ADMIN);
        map.put("password", PASSWORD_ADMIN);
        map.put("roles", "PAGE_1,PAGE_2");
        JsonObject response = getJsonResponsePost("http://localhost:15000/user", map, USERNAME_ADMIN, PASSWORD_ADMIN);

        JsonElement code = response.get("code");
        JsonElement message = response.get("message");

        Assert.assertEquals(code.getAsInt(), Constants.CONFLICT_CODE);
        Assert.assertEquals(message.getAsString(), Constants.USER_EXISTS);
    }

}
