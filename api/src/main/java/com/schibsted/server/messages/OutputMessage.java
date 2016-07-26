package com.schibsted.server.messages;

/**
 * Created by edu on 24/07/2016.
 */
public class OutputMessage {

    public static OutputMessage build(String code, String message){
        return  new OutputMessage(code, message);
    }

    private OutputMessage(String code, String message) {
        this.code = code;
        this.message = message;
    }

    private String code;
    private String message;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
