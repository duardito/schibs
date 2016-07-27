package com.schibsted.server.messages.user;

import com.schibsted.common.Constants;
import com.schibsted.server.messages.MessageWrapperException;
import com.schibsted.server.messages.OutputMessage;

import java.io.OutputStream;

/**
 * Created by edu on 26/07/2016.
 */
public class UserfieldsRequired extends MessageWrapperException {

    public UserfieldsRequired(OutputStream out) throws Exception {
        super(out, OutputMessage.build(Constants.BAD_REQUEST_CODE,Constants.FIELDS_REQUIRED) );
    }
}
