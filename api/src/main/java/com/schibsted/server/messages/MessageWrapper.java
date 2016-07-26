package com.schibsted.server.messages;

import com.google.gson.Gson;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;

/**
 * Created by edu on 24/07/2016.
 */
public abstract class MessageWrapper{

    public MessageWrapper(){
    }

    public  MessageWrapper (OutputStream out, Object outputMessage) throws IOException {
        final Gson gson = new Gson();
        Writer w = new OutputStreamWriter(out);
        w.write(gson.toJson(outputMessage));
        w.flush();
        w.close();
    }



}
