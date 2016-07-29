package com.schibsted.server.messages.base;

import com.google.gson.Gson;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;

public abstract class BaseMessageWrapper {

    public BaseMessageWrapper(){
    }

    public  BaseMessageWrapper (final OutputStream out,final Object outputMessage) throws IOException {
        final Gson gson = new Gson();
        final Writer w = new OutputStreamWriter(out);
        w.write(gson.toJson(outputMessage));
        w.flush();
        w.close();
    }
}
