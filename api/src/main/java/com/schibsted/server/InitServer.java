package com.schibsted.server;

import com.schibsted.server.handler.SwitchHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;

/**
 * Created by edu on 23/07/2016.
 */
public class InitServer {

    private static HttpServer server;

    public static void startServer() throws IOException {
        server = HttpServer.create(new InetSocketAddress(15000), 0);
        server.createContext("/", new SwitchHandler());
        server.start();
    }

    public static void stopServer() throws IOException {
        server.stop(1);
    }
}
