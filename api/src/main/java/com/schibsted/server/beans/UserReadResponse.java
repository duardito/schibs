package com.schibsted.server.beans;

import com.schibsted.domain.user.User;
import com.schibsted.server.beans.base.BaseUserResponse;

/**
 * Created by edu on 26/7/16.
 */
public class UserReadResponse extends BaseUserResponse {

    public UserReadResponse(User user) {
        super(user.getUsername(), user.getRoles());
    }
}
