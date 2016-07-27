package com.schibsted.server.exception;

import com.schibsted.common.Constants;
import com.schibsted.server.messages.MessageWrapperException;
import com.schibsted.server.messages.OutputMessage;

import java.io.OutputStream;

/**
 * Created by edu on 26/07/2016.
 */
public class UnathorizedException extends MessageWrapperException {

    public UnathorizedException(OutputStream out) throws Exception {
        super(out, OutputMessage.build(Constants.UNATHORIZED_CODE,Constants.UNATHORIZED) );
    }
}
