package com.schibsted.server.messages.page;

import com.schibsted.server.messages.MessageWrapper;

import java.io.OutputStream;

public class PageMessageApi extends MessageWrapper {

    public PageMessageApi(final OutputStream out,final Object o) throws Exception {
        super(out, o);
    }

}
