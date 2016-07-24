package com.schibsted.domain.user;

import com.schibsted.domain.role.Role;

import java.io.Serializable;
import java.util.LinkedHashSet;

/**
 * Created by edu on 23/07/2016.
 */
public class User implements Serializable {

    private String username;
    private String password;
    private LinkedHashSet<Role> roles = new LinkedHashSet<Role>();

    public static User build(final String username,final String password, final LinkedHashSet<Role> roles){
        return new User(username, password, roles);
    }

    private User(final String username, final String password, final LinkedHashSet<Role> roles) {
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

    public LinkedHashSet<Role> getRoles() {
        return roles;
    }

    public void setRoles(LinkedHashSet<Role> roles) {
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
