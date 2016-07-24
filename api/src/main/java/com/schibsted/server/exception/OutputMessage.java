package com.schibsted.server.exception;

import java.io.Serializable;

/**
 * Created by edu on 24/07/2016.
 */
public class OutputMessage implements Serializable{

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
