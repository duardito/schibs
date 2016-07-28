package com.schibsted.api.page;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.schibsted.common.Constants;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;

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
        String passwd ="12345";

        JsonObject resp = getJsonResponsePostForLogin("http://localhost:15000/login", edu,passwd);
        String auth = resp.get("auth").getAsString();

        String url = "http://localhost:15000/"+USER_PAGE_1;

        JsonObject response = getJsonResponseGet(url, auth);
        JsonElement pagename = response.get("pagename");
        JsonElement username = response.get("username");

        Assert.assertEquals(username.getAsString(), edu);
        Assert.assertEquals(pagename.getAsString(), USER_PAGE_1);
    }


    @Test
    public void getAccessPage1NoPermissionsNO_OK() throws IOException {

        String toni = "toni";
        String password ="5678";

        JsonObject resp = getJsonResponsePostForLogin("http://localhost:15000/login", toni,password);
        String auth = resp.get("auth").getAsString();

        String url = "http://localhost:15000/"+USER_PAGE_1;


        JsonObject response = getJsonResponseGet(url, auth);
        JsonElement code = response.get("code");
        JsonElement message = response.get("message");

        Assert.assertEquals(code.getAsInt(), Constants.UNATHORIZED_CODE);
        Assert.assertEquals(message.getAsString(), Constants.USER_HAS_NOT_PERMISSIONS);
    }


    @Test
    public void getAccessPage2_same_user_juan() throws IOException {


        JsonObject resp = getJsonResponsePostForLogin("http://localhost:15000/login", JUAN,JUAN_PASSWORD);
        String auth = resp.get("auth").getAsString();

        String url = "http://localhost:15000/"+USER_PAGE_2;

        JsonObject response = getJsonResponseGet(url, auth);
        JsonElement pagename = response.get("pagename");
        JsonElement username = response.get("username");

        Assert.assertEquals(username.getAsString(), JUAN);
        Assert.assertEquals(pagename.getAsString(), USER_PAGE_2);
    }

    @Test
    public void getAccessPage3_same_user_juan() throws IOException {

        JsonObject resp = getJsonResponsePostForLogin("http://localhost:15000/login", JUAN,JUAN_PASSWORD);
        String auth = resp.get("auth").getAsString();

        String url = "http://localhost:15000/"+USER_PAGE_3;

        JsonObject response = getJsonResponseGet(url, auth);
        JsonElement pagename = response.get("pagename");
        JsonElement username = response.get("username");

        Assert.assertEquals(username.getAsString(), JUAN);
        Assert.assertEquals(pagename.getAsString(), USER_PAGE_3);
    }

}
