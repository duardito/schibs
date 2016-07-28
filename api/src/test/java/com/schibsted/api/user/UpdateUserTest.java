package com.schibsted.api.user;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.schibsted.common.Constants;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class UpdateUserTest extends BaseUserTest {

    @Test
    public void updateUserOK() throws IOException {


        final String modPassword = "admin1234";
        final String usernameInit = "edu";

        Map<String, String> map = new HashMap<>();
        map.put("username", usernameInit);
        map.put("password", modPassword);
        map.put("roles", "PAGE_1,PAGE_2");
        JsonObject response = getJsonResponsePut("http://localhost:15000/user", map, login());
        JsonElement username = response.get("username");
        JsonElement password = response.get("password");

        Assert.assertEquals(username.getAsString(), usernameInit);
        Assert.assertEquals(password.getAsString(), modPassword);
    }

    @Test
    public void updateUserFailOnlyOneAdminNO_OK() throws IOException {


        Map<String, String> map = new HashMap<>();
        map.put("username", USERNAME_ADMIN);
        map.put("password", PASSWORD_ADMIN);
        map.put("roles", "PAGE_1,PAGE_2");
        JsonObject response = getJsonResponsePut("http://localhost:15000/user", map, login());
        JsonElement code = response.get("code");
        JsonElement message = response.get("message");

        Assert.assertEquals(code.getAsInt(), Constants.CONFLICT_CODE);
        Assert.assertEquals(message.getAsString(), Constants.FAILED_UPDATE_ADMIN);
    }
}
