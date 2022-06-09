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
        // Логируем информацию о подключенном пользователе
    }

    @Override
    public void onClose(WebSocket webSocket, int i, String s, boolean b) {
        logger.info("Disconnected client: " + webSocket.getRemoteSocketAddress().getHostString() + ":" + webSocket.getRemoteSocketAddress().getPort());
        // Логируем информацию о отключеном пользователе
        logger.info("Disconnected nick: " + clientHashMap.get(webSocket).getNickname());
        this.clientHashMap.remove(webSocket);
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
                Events.joinNewClient(jsonNode.get("data"),webSocket,jsonNode.get("requestID").asInt(),this.clientHashMap); // Запускаем ивент входа новога пользователя
                break;
            }
            case "message" : { // Отправляем сообщение пользоватям
                logger.trace("Type is message");
                Events.sendMessage(jsonNode.get("data"),webSocket, jsonNode.get("requestID").asInt(),this.clientHashMap); // Запускаем ивент отправки сообщения
                break;
            }
        }
    }



}
