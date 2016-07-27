package com.schibsted.server.exception;

import com.schibsted.common.Constants;
import com.schibsted.server.messages.MessageWrapperException;
import com.schibsted.server.messages.OutputMessage;

import java.io.OutputStream;

/**
 * Created by edu on 27/7/16.
 */
public class UserHasNotPermissionsException extends MessageWrapperException {

    public UserHasNotPermissionsException(OutputStream out) throws Exception {
        super(out, OutputMessage.build(Constants.UNATHORIZED_CODE,Constants.USER_HAS_NOT_PERMISSIONS) );
    }
}
