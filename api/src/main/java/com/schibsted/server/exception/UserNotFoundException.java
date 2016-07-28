package com.schibsted.server.exception;

import com.schibsted.common.Constants;
import com.schibsted.server.messages.MessageWrapperException;
import com.schibsted.server.messages.OutputMessage;

import java.io.OutputStream;

/**
 * Created by edu on 27/7/16.
 */
public class UserNotFoundException extends MessageWrapperException {

    public UserNotFoundException(OutputStream out) throws Exception {
        super(out, OutputMessage.build(Constants.UNATHORIZED_CODE,Constants.USER_NOT_FOUND) );
    }
}
