package com.schibsted.api.user;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.schibsted.common.Constants;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;

public class GetUserTest extends BaseUserTest {


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
    public void getUserNotExistsNO_OK() throws IOException {
        JsonObject response = getJsonResponseGet("http://localhost:15000/user/juli", USERNAME_ADMIN, PASSWORD_ADMIN);

        JsonElement code = response.get("code");
        JsonElement message = response.get("message");

        Assert.assertEquals(code.getAsInt(), Constants.CONFLICT_CODE);
        Assert.assertEquals(message.getAsString(), Constants.USER_NOT_FOUND);
    }






}
