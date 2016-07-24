package com.schibsted.server;

import com.schibsted.server.handler.SwitchHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;

/**
 * Created by edu on 23/07/2016.
 */
public class InitServer {

    public static void startServer() throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(15000), 0);
        server.createContext("/", new SwitchHandler());
        server.start();
    }
}
