package com.schibsted.server.messages.user;

import com.schibsted.common.Constants;
import com.schibsted.server.messages.MessageWrapper;
import com.schibsted.server.messages.OutputMessage;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by edu on 26/07/2016.
 */
public class UserfieldsRequired extends MessageWrapper {

    public UserfieldsRequired(OutputStream out) throws IOException {
        super(out, OutputMessage.build(Constants.BAD_REQUEST_CODE,Constants.FIELDS_REQUIRED) );
    }
}
