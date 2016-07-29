package com.schibsted.server.messages;

/**
 * Created by edu on 28/7/16.
 */
public final class LoginMessage {

    public static LoginMessage build(final String auth){
        return new LoginMessage( auth);
    }

    private LoginMessage(final String auth) {
        this.auth = auth;

    }

    private String auth;

    public String getAuth() {
        return auth;
    }

}
