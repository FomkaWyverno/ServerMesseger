package com.wyverno.server.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class ReceiveClients implements Runnable {

    private final Server server;
    private final ServerSocket serverSocket;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public ReceiveClients(Server server, ServerSocket serverSocket) {
        this.server = server;
        this.serverSocket = serverSocket;
    }

    @Override
    public void run() {
        while (true) {
            Socket clientSocket = null;
            BufferedReader reader = null;
            Client client = null;
            try {
                clientSocket = serverSocket.accept();
                reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                client = handshake(reader.readLine(),clientSocket);

            } catch (IOException e) {
                e.printStackTrace();
            }

            if (client != null) {
                server.addClient(clientSocket);
            }
        }
    }

    public Client handshake(String JSON, Socket socket) {
        Client client = null;
        try {
            client = objectMapper.readValue(JSON, Client.class);
            client.setSocket(socket);
        } catch (JsonProcessingException e) {
            System.out.println("Рукопожатие не произошло");
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return client;
    }
}
