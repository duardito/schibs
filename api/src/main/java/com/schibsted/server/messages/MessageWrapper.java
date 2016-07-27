package com.schibsted.server.messages;

import com.schibsted.server.messages.base.BaseMessageWrapper;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by edu on 24/07/2016.
 */
public abstract class MessageWrapper extends BaseMessageWrapper {

    public MessageWrapper(){
    }

    public  MessageWrapper (OutputStream out, Object outputMessage) throws IOException {
        super(out,outputMessage);
    }



}
