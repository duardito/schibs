package com.schibsted.server.exception;

import com.schibsted.common.Constants;
import com.schibsted.server.messages.MessageWrapperException;
import com.schibsted.server.messages.OutputMessage;

import java.io.OutputStream;

/**
 * Created by edu on 27/7/16.
 */
public class UpdateNotAllowedException extends MessageWrapperException {

    public UpdateNotAllowedException(OutputStream out) throws Exception {
        super(out, OutputMessage.build(Constants.CONFLICT_CODE,Constants.FAILED_UPDATE_ADMIN) );
    }
}
