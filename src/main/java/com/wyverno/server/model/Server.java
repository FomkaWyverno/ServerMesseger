package com.wyverno.server.model;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server {
    private ServerSocket server;
    private ArrayList<Socket> clientList = new ArrayList<>();

    public Server(int port) throws IOException {
        this.server = new ServerSocket(port);
    }

    public Server() throws IOException {
        this(4747);
    }

    public void close() throws IOException {
        server.close();
    }
}
