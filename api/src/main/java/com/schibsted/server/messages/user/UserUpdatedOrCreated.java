package com.schibsted.server.messages.user;

import com.schibsted.server.messages.MessageWrapper;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by edu on 26/07/2016.
 */
public class UserUpdatedOrCreated extends MessageWrapper {

    public UserUpdatedOrCreated(OutputStream out, Object o) throws IOException {
        super(out, o);
    }
}
