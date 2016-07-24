package com.schibsted.server.exception;

import com.google.gson.Gson;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

/**
 * Created by edu on 24/07/2016.
 */
public class MessageWrapper {

    public static MessageWrapper build(OutputStream out, OutputMessage outputMessage) throws IOException {
        return new MessageWrapper(out, outputMessage);
    }

    private MessageWrapper(OutputStream out, OutputMessage outputMessage) throws IOException {
        final Gson gson = new Gson();
        final ObjectOutputStream objOut = new ObjectOutputStream(out);
        objOut.writeObject(gson.toJson(outputMessage));
        objOut.close();
    }


}
