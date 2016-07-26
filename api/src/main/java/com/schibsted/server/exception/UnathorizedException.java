package com.schibsted.server.exception;

import com.schibsted.server.messages.MessageWrapper;
import com.schibsted.server.messages.OutputMessage;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by edu on 26/07/2016.
 */
public class UnathorizedException extends MessageWrapper {

    public UnathorizedException(OutputStream out) throws IOException {
        super(out, OutputMessage.build("403","unathorized access") );
    }
}
