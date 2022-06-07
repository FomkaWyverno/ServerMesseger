package com.wyverno.server.model;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server {
    private final ServerSocket socket;
    private final Thread receiveClientsThread;
    private final ArrayList<Socket> clientList = new ArrayList<>();

    public Server(int port) throws IOException {
        this.socket = new ServerSocket(port);
        this.receiveClientsThread = new Thread(new ReceiveClients(this, this.socket));
        this.receiveClientsThread.start();
    }

    protected void addClient(Socket client) {
        this.clientList.add(client);
    }

    public void close() throws IOException {
        receiveClientsThread.interrupt(); // Прекратить подключать пользователей
        socket.close(); // Закрыть сервер
    }
}
