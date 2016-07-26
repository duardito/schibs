package com.schibsted.server.exception;

import com.schibsted.common.Constants;
import com.schibsted.server.messages.MessageWrapper;
import com.schibsted.server.messages.OutputMessage;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by edu on 26/7/16.
 */
public class BadRequestException extends MessageWrapper {

    public BadRequestException(OutputStream out) throws IOException {
        super(out, OutputMessage.build(Constants.BAD_REQUEST_CODE,Constants.BAD_REQUEST) );
    }
}
