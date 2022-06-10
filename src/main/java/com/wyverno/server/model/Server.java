package com.wyverno.server.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wyverno.server.model.client.Client;
import com.wyverno.server.model.client.chat.Chat;
import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;
import org.slf4j.LoggerFactory;

import org.slf4j.Logger;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Server extends WebSocketServer {

    private static final Logger logger = LoggerFactory.getLogger(Server.class);
    private static final ObjectMapper objectMapper = new ObjectMapper();

    private static final int DEFAULT_PORT = 4747;


    private final HashMap<WebSocket,Client> clientHashMap = new HashMap<>();
    private final List<Chat> chatList;
    private final Chat GLOBAL_CHAT;

    public Server (InetSocketAddress address, List<Chat> chatList, Chat globalChat) {
        super(address);
        this.chatList = chatList;
        this.GLOBAL_CHAT = globalChat;
    }

    public Server(int port, List<Chat> chatList, Chat globalChat) {
        super(new InetSocketAddress(port));
        this.chatList = chatList;
        this.GLOBAL_CHAT = globalChat;
    }

    public Server(InetSocketAddress address, Chat globalChat) {
        super(address);
        this.chatList = new ArrayList<>();
        this.GLOBAL_CHAT = globalChat;
    }

    public Server(int port, Chat globalChat) {
        super(new InetSocketAddress(port));
        this.chatList = new ArrayList<>();
        this.GLOBAL_CHAT = globalChat;
    }

    public Server(Chat globalChat) {
        super(new InetSocketAddress(DEFAULT_PORT));
        this.chatList = new ArrayList<>();
        this.GLOBAL_CHAT = globalChat;
    }



    @Override
    public void onOpen(WebSocket webSocket, ClientHandshake clientHandshake) {
        logger.info("Connected client: " + webSocket.getRemoteSocketAddress().getHostString() + ":" + webSocket.getRemoteSocketAddress().getPort());
        // Логируем информацию о подключенном пользователе
    }

    @Override
    public void onClose(WebSocket webSocket, int i, String s, boolean b) {
        logger.info("Disconnected client: " + webSocket.getRemoteSocketAddress().getHostString() + ":" + webSocket.getRemoteSocketAddress().getPort());
        // Логируем информацию о отключеном пользователе
        Client client = clientHashMap.get(webSocket);
        logger.info("Disconnected nick: " + client.getNickname());
        this.clientHashMap.remove(webSocket);
        Chat clientChat = client.getRightNowChat();
        clientChat.leaveClient(client);
    }

    @Override
    public void onMessage(WebSocket webSocket, String message) {
        logger.debug("Received a message from a client: " + message); // Логируем сообщение от пользователя
        logger.debug("Received from the user " + webSocket.getRemoteSocketAddress()); // Логируем от кого пришло сообщение
        try {
            JsonNode jsonNode = objectMapper.readTree(message); // Парсим в JsonNode сообжение
            logger.trace("Read json"); // Оповещаем что мы прочитали json
            processingRequest(jsonNode,webSocket); // Вызываем метод процесса обработки запроса
        } catch (JsonProcessingException e) {
            logger.error("The message is not JSON or the message is have bug with JSON file.\n" + e.getCause()); // Оповещаем о ошибке в логгер
        }
    }

    @Override
    public void onError(WebSocket webSocket, Exception e) {

    }

    @Override
    public void onStart() {
        logger.info("Server started ip-address is " + this.getAddress().getHostName() + ":" + this.getPort());
        // Оповещаем логера о запуске сервера
    }


    private void processingRequest(JsonNode jsonNode, WebSocket webSocket) throws JsonProcessingException {
        logger.trace("Processing request...");
        switch (jsonNode.get("data").get("type").asText()) { // Узнаем тип запроса
            case "authorization" : { // Проводим авторизацию пользователя
                logger.trace("Type is authorization");
                Events.joinNewClient(jsonNode.get("data"),
                        webSocket,jsonNode.get("requestID").asInt(),
                        this.clientHashMap,
                        this.GLOBAL_CHAT); // Запускаем ивент входа новога пользователя
                break;
            }
            case "message" : { // Отправляем сообщение пользоватям
                logger.trace("Type is message");
                Events.sendMessage(jsonNode.get("data"),webSocket, jsonNode.get("requestID").asInt(),this.clientHashMap); // Запускаем ивент отправки сообщения
                break;
            }
        }
    }

    public void addChat(Chat chat) {
        this.chatList.add(chat);
    }
}
