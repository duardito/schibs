package com.schibsted.domain.role;

import java.io.Serializable;

/**
 * Created by edu on 23/07/2016.
 */
public class Role implements Serializable {

    private String roleName;

    public static Role build(final String roleName){
        return new Role(roleName);
    }

    private Role(final String roleName) {
        this.roleName = roleName;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    @Override
    public String toString() {
        return "Role{" +
                "roleName='" + roleName + '\'' +
                '}';
    }
}
