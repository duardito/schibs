package com.schibsted.server.messages;

/**
 * Created by edu on 24/07/2016.
 */
public class OutputMessage {

    public static OutputMessage build(final int code,final String message){
        return  new OutputMessage(code, message);
    }

    private OutputMessage(int code, String message) {
        this.code = code;
        this.message = message;
    }

    private int code;
    private String message;

    public int getCode() {
        return code;
    }


    public String getMessage() {
        return message;
    }

}
