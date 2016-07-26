package com.schibsted.server.exception;

import com.google.gson.Gson;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Optional;

/**
 * Created by edu on 24/07/2016.
 */
public class MessageWrapper {

    public static MessageWrapper build(OutputStream out, Optional <?> outputMessage) throws IOException {
        return new MessageWrapper(out, outputMessage);
    }

    private MessageWrapper(OutputStream out, Optional <?> outputMessage) throws IOException {
        final Gson gson = new Gson();
        Writer w = new OutputStreamWriter(out);
        w.write(gson.toJson(outputMessage));
        w.flush();
        w.close();
    }


}
