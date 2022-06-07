package com.wyverno.server.view;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class Client {
    private final String username;
    private final Socket socket;
    private final BufferedWriter writer;

    public Client(String username, Socket socket) throws IOException {
        this.username = username;
        this.socket = socket;
        this.writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
    }

    public void sendMessage(String message) throws IOException {
        this.writer.write(message);
        this.writer.flush();
    }
}
