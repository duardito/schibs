package com.schibsted.server.beans.user;

import com.schibsted.domain.user.User;
import com.schibsted.server.beans.base.BaseUserResponse;

/**
 * Created by edu on 26/7/16.
 */
public class UserCompleteResponse extends BaseUserResponse {

    public UserCompleteResponse(final User user){
        super(user.getUsername(), user.getRoles());
        this.password=user.getPassword();
    }

    private String password;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
