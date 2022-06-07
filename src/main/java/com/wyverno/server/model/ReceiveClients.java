package com.wyverno.server.model;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ReceiveClients implements Runnable {

    private final Server server;
    private final ServerSocket serverSocket;

    public ReceiveClients(Server server, ServerSocket serverSocket) {
        this.server = server;
        this.serverSocket = serverSocket;
    }

    @Override
    public void run() {
        while (true) {
            Socket client = null;
            try {
                client = serverSocket.accept();
            } catch (IOException e) {
                e.printStackTrace();
            }
            assert client != null;
            server.addClient(client);
        }
    }
}
