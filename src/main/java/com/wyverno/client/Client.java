package com.wyverno.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft;
import org.java_websocket.handshake.ServerHandshake;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;


public class Client extends WebSocketClient {

    private static final ObjectMapper objectMapper = new ObjectMapper();


    public static void main(String[] args) throws URISyntaxException, IOException, InterruptedException {
        WebSocketClient client = new Client("ws://localhost:50");
        client.connect();
        while (!client.isOpen()) {
            Thread.sleep(1000);
        }
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        System.out.print("Введите свое имя: ");
        User user = new User(reader.readLine());
        client.send(Protocol.getJsonMessage(user));

        while (true) {
            System.out.println("Введите сообщение ");
            String protocolMessage = Protocol.getJsonMessage(new Message(reader.readLine()));
            client.send(protocolMessage);
        }
    }

    public Client(URI serverUri, Draft draft) {
        super(serverUri, draft);
    }

    public Client(URI serverURI) {
        super(serverURI);
    }

    public Client(String URI) throws URISyntaxException {
        super(new URI(URI));
    }



    @Override
    public void onOpen(ServerHandshake serverHandshake) {

    }

    @Override
    public void onMessage(String s) {
        try {
            JsonNode jsonNode = objectMapper.readTree(s);
            System.out.println(jsonNode.get("nickname").asText() + ": " + jsonNode.get("message").asText());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onClose(int i, String s, boolean b) {

    }

    @Override
    public void onError(Exception e) {

    }
}
