package com.schibsted.domain.role;

/**
 * Created by edu on 23/07/2016.
 */
public class Role {

    private String roleName;

    private static Role build(final String roleName){
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
}
