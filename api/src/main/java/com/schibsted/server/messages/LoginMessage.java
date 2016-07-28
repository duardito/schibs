package com.schibsted.server.messages;

/**
 * Created by edu on 28/7/16.
 */
public final class LoginMessage {

    public static LoginMessage build(String auth){
        return new LoginMessage( auth);
    }

    private LoginMessage(String auth) {
        this.auth = auth;

    }

    private String auth;

    public String getAuth() {
        return auth;
    }

}
