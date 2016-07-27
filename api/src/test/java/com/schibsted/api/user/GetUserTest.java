package com.schibsted.api.user;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.schibsted.common.Constants;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class GetUserTest extends BaseUserTest {


    @Test
    public void getUserOK() throws IOException {
        Map<String, String> map = new HashMap<>();
        map.put("username", USERNAME_ADMIN);
        map.put("password", PASSWORD_ADMIN);
        getJsonResponsePost("http://localhost:15000/login", map, USERNAME_ADMIN, PASSWORD_ADMIN);

        JsonObject response = getJsonResponseGet("http://localhost:15000/user/edu", USERNAME_ADMIN, PASSWORD_ADMIN);

        JsonElement username = response.get("username");
        JsonElement password = response.get("password");

        Assert.assertEquals(username.getAsString(), "edu");
        Assert.assertNull(password);
    }

    @Test
    public void getUserBadRequestNO_OK() throws IOException {

        Map<String, String> map = new HashMap<>();
        map.put("username", USERNAME_ADMIN);
        map.put("password", PASSWORD_ADMIN);
        getJsonResponsePost("http://localhost:15000/login", map, USERNAME_ADMIN, PASSWORD_ADMIN);

        JsonObject response = getJsonResponseGet("http://localhost:15000/user", USERNAME_ADMIN, PASSWORD_ADMIN);
        JsonElement code = response.get("code");
        JsonElement message = response.get("message");

        Assert.assertEquals(code.getAsInt(), Constants.BAD_REQUEST_CODE);
        Assert.assertEquals(message.getAsString(), Constants.BAD_REQUEST);
    }

    @Test
    public void getUserNotExistsNO_OK() throws IOException {

        Map<String, String> map = new HashMap<>();
        map.put("username", USERNAME_ADMIN);
        map.put("password", PASSWORD_ADMIN);
        getJsonResponsePost("http://localhost:15000/login", map, USERNAME_ADMIN, PASSWORD_ADMIN);

        JsonObject response = getJsonResponseGet("http://localhost:15000/user/juli", USERNAME_ADMIN, PASSWORD_ADMIN);

        JsonElement code = response.get("code");
        JsonElement message = response.get("message");

        Assert.assertEquals(code.getAsInt(), Constants.CONFLICT_CODE);
        Assert.assertEquals(message.getAsString(), Constants.USER_NOT_FOUND);
    }

}
