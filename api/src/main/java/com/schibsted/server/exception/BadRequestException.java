package com.schibsted.server.exception;

import com.schibsted.common.Constants;
import com.schibsted.server.messages.MessageWrapperException;
import com.schibsted.server.messages.OutputMessage;

import java.io.OutputStream;

/**
 * Created by edu on 26/7/16.
 */
public class BadRequestException extends MessageWrapperException {

    public BadRequestException(OutputStream out) throws Exception {
        super(out, OutputMessage.build(Constants.BAD_REQUEST_CODE,Constants.BAD_REQUEST) );
    }
}
