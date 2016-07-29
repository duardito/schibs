package com.schibsted.server.messages;

import com.google.gson.Gson;

import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;

public abstract class MessageWrapperException extends Exception {

    public MessageWrapperException(){
    }

    public  MessageWrapperException (OutputStream out, Object outputMessage) throws Exception {
        final Gson gson = new Gson();
        Writer w = new OutputStreamWriter(out);
        w.write(gson.toJson(outputMessage));
        w.flush();
        w.close();
    }

}
