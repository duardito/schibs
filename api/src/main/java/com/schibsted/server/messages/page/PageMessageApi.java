package com.schibsted.server.messages.page;

import com.schibsted.server.messages.MessageWrapper;

import java.io.OutputStream;

/**
 * Created by edu on 27/7/16.
 */
public class PageMessageApi extends MessageWrapper {

    public PageMessageApi(OutputStream out, Object o) throws Exception {
        super(out, o);
    }

}
