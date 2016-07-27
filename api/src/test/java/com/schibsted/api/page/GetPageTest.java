package com.schibsted.api.page;

import com.google.gson.JsonObject;
import org.junit.Test;

import java.io.IOException;

public class GetPageTest extends BasePageTest {


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
        JsonObject response = getJsonResponseGet("http://localhost:15000/Page%201", "edu", "12345");
        System.out.println(response);
    }
}
