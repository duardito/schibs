package com.schibsted.server.messages.user;

import com.schibsted.server.messages.MessageWrapper;

import java.io.OutputStream;

/**
 * Created by edu on 26/07/2016.
 */
public class UserMessage extends MessageWrapper {

    public UserMessage(OutputStream out, Object o) throws Exception {
        super(out, o);
    }
}
