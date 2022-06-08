package com.wyverno.server.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wyverno.server.model.client.Client;
import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;
import org.slf4j.LoggerFactory;

import org.slf4j.Logger;

import java.net.InetSocketAddress;
import java.util.HashMap;

public class Server extends WebSocketServer {

    private static final Logger logger = LoggerFactory.getLogger(Server.class);
    private static final ObjectMapper objectMapper = new ObjectMapper();

    private final HashMap<WebSocket,Client> clientHashMap = new HashMap<>();

    public Server(InetSocketAddress address) {
        super(address);
    }

    public Server(int port) {
        super(new InetSocketAddress(port));
    }

    @Override
    public void onOpen(WebSocket webSocket, ClientHandshake clientHandshake) {
        logger.info("Connected client: " + webSocket.getRemoteSocketAddress().getHostString() + ":" + webSocket.getRemoteSocketAddress().getPort());
    }

    @Override
    public void onClose(WebSocket webSocket, int i, String s, boolean b) {

    }

    @Override
    public void onMessage(WebSocket webSocket, String message) {
        logger.info("Received a message from a client");
        try {
            JsonNode jsonNode = objectMapper.readTree(message);
            processingRequest(jsonNode,webSocket);
        } catch (JsonProcessingException e) {
            logger.error(String.valueOf(e.getCause()));
        }
    }

    @Override
    public void onError(WebSocket webSocket, Exception e) {

    }

    @Override
    public void onStart() {
        logger.info("Server started ip-address is " + this.getAddress().getHostName() + ":" + this.getPort());
    }


    private void processingRequest(JsonNode jsonNode, WebSocket webSocket) {
        switch (jsonNode.get("type").asText()) {
            case "nickname" : {
                Client client = new Client(jsonNode.get("nick").asText(),webSocket);
                clientHashMap.put(webSocket,client);
            }
            case "message" : {
                Client client = clientHashMap.get(webSocket);
                
            }
        }
    }
}
