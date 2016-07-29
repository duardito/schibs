package com.schibsted.domain.user;

import java.io.Serializable;
import java.util.LinkedHashSet;

public class User implements Serializable {

    private String username;
    private String password;
    private LinkedHashSet<String> roles = new LinkedHashSet<String>();

    public static User build(final String username,final String password, final LinkedHashSet<String> roles){
        return new User(username, password, roles);
    }

    private User(final String username, final String password, final LinkedHashSet<String> roles) {
        this.username = username;
        this.password = password;
        this.roles = roles;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public LinkedHashSet<String> getRoles() {
        return roles;
    }

    public void setRoles(LinkedHashSet<String> roles) {
        this.roles = roles;
    }

    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", roles=" + roles +
                '}';
    }
}
