package com.schibsted.api.page;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.schibsted.common.Constants;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class GetPageTest extends BasePageTest{

        private static final String JUAN ="juan";
        private static final String JUAN_PASSWORD ="7788";


    /*
     LinkedHashSet<String> role = new LinkedHashSet<String>();
        role.add(Constants.PAGE_1);
        User userPage1 = User.build("edu", "12345", role);

        userList.add(userPage1);

        role = new LinkedHashSet<String>();
        role.add(Constants.PAGE_2);
        User userPage2 = User.build("toni", "5678", role);

        userList.add(userPage2);

        role = new LinkedHashSet<String>();
        role.add(Constants.PAGE_3);
        role.add(Constants.PAGE_2);
        User userPage3 = User.build("juan", "7788", role);

        userList.add(userPage3);
     */

    @Test
    public void getAccessPage1_OK() throws IOException {

        String edu = "edu";
        String url = "http://localhost:15000/"+USER_PAGE_1;

        JsonObject response = getJsonResponseGet(url, edu, "12345");
        JsonElement pagename = response.get("pagename");
        JsonElement username = response.get("username");

        Assert.assertEquals(username.getAsString(), edu);
        Assert.assertEquals(pagename.getAsString(), USER_PAGE_1);
    }

    @Test
    public void getAccessPage1NoPermissionsNO_OK() throws IOException {

        String toni = "toni";
        String password ="5678";
        String url = "http://localhost:15000/"+USER_PAGE_1;

        Map<String, String> map = new HashMap<>();
        map.put("username", toni);
        map.put("password", password);
        getJsonResponsePost("http://localhost:15000/login", map, toni, password);


        JsonObject response = getJsonResponseGet(url, toni, "5678");
        JsonElement code = response.get("code");
        JsonElement message = response.get("message");

        Assert.assertEquals(code.getAsInt(), Constants.UNATHORIZED_CODE);
        Assert.assertEquals(message.getAsString(), Constants.USER_HAS_NOT_PERMISSIONS);
    }


    @Test
    public void getAccessPage2_same_user_juan() throws IOException {

        String url = "http://localhost:15000/"+USER_PAGE_2;

        JsonObject response = getJsonResponseGet(url, JUAN, JUAN_PASSWORD);
        JsonElement pagename = response.get("pagename");
        JsonElement username = response.get("username");

        Assert.assertEquals(username.getAsString(), JUAN);
        Assert.assertEquals(pagename.getAsString(), USER_PAGE_2);
    }

    @Test
    public void getAccessPage3_same_user_juan() throws IOException {

        String url = "http://localhost:15000/"+USER_PAGE_3;

        JsonObject response = getJsonResponseGet(url, JUAN, JUAN_PASSWORD);
        JsonElement pagename = response.get("pagename");
        JsonElement username = response.get("username");

        Assert.assertEquals(username.getAsString(), JUAN);
        Assert.assertEquals(pagename.getAsString(), USER_PAGE_3);
    }

}
