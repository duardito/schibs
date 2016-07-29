package com.schibsted.server.messages.user;

import com.schibsted.server.messages.MessageWrapper;

import java.io.OutputStream;


public class UserMessage extends MessageWrapper {

    public UserMessage(final OutputStream out,final Object o) throws Exception {
        super(out, o);
    }
}
