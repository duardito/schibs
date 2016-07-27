package com.schibsted.server.beans.base;

import java.util.LinkedHashSet;

/**
 * Created by edu on 26/7/16.
 */
public abstract class BaseUserResponse {

    public BaseUserResponse(String username, LinkedHashSet<String> roles) {
        this.username = username;
        this.roles = roles;
    }

    private String username;
    private LinkedHashSet<String> roles = new LinkedHashSet<String>();

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public LinkedHashSet<String> getRoles() {
        return roles;
    }

    public void setRoles(LinkedHashSet<String> roles) {
        this.roles = roles;
    }
}
