package com.schibsted.server.messages.base;

import com.google.gson.Gson;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;

/**
 * Created by edu on 27/7/16.
 */
public abstract class BaseMessageWrapper {

    public BaseMessageWrapper(){
    }

    public  BaseMessageWrapper (OutputStream out, Object outputMessage) throws IOException {
        final Gson gson = new Gson();
        Writer w = new OutputStreamWriter(out);
        w.write(gson.toJson(outputMessage));
        w.flush();
        w.close();
    }
}
