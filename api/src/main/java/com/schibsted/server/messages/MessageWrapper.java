package com.schibsted.server.messages;

import com.schibsted.server.messages.base.BaseMessageWrapper;

import java.io.IOException;
import java.io.OutputStream;

public abstract class MessageWrapper extends BaseMessageWrapper {

    public MessageWrapper(){
    }

    public  MessageWrapper (OutputStream out, Object outputMessage) throws IOException {
        super(out,outputMessage);
    }



}
