package com.schibsted.server.security;

import com.schibsted.service.IUserService;
import com.schibsted.service.UserServiceImpl;

/**
 * Created by edu on 24/07/2016.
 */
public class SecurityUtils {

    private IUserService userService;

    public SecurityUtils() {
        if(userService == null){
            userService = new UserServiceImpl();
        }
    }


}
