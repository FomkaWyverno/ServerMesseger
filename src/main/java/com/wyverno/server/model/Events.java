package com.wyverno.server.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wyverno.server.model.client.Client;
import com.wyverno.server.model.client.Message;
import org.java_websocket.WebSocket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class Events {

    private static final Logger logger = LoggerFactory.getLogger(Events.class);
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static void joinNewClient(JsonNode jsonNode, WebSocket webSocket, HashMap<WebSocket,Client> clientHashMap) {
        Client client = new Client(jsonNode.get("nick").asText(),webSocket); // Создаем нового пользователя сервера
        clientHashMap.put(webSocket,client); // Кладем его в мапу под ключем сокета
        logger.debug("Put in hashMap new client HashMap -> " + clientHashMap.toString());
    }

    public static void sendMessage(JsonNode jsonNode, WebSocket webSocket, HashMap<WebSocket, Client> clientHashMap) throws JsonProcessingException {
        Client client = clientHashMap.get(webSocket); // Берем у Сокета обьект Клиента
        logger.debug("HashMap ->" + clientHashMap.toString()); // Показываем внутреность карты
        logger.debug("Get client object from clientHashMap: " + client.toString()); // Оповещаем что мы взяли из карты клиента


        String nickname = client.getNickname(); // Узнаем никнейм
        logger.debug("Get client nickname: " + nickname); // Логгируем никнейм


        String messageClient = jsonNode.get("message").asText(); // Берем у запроса сообщение
        logger.debug("Get client message: " + messageClient); // Логируем сообщение


        Message message = new Message(nickname,messageClient); // Создаем обьект сообщение который включает в себе никнейм и сообщение
        logger.debug("Created message object");


        String jsonAnswer = objectMapper.writeValueAsString(message); // Создаем JSON ответ из обьекта message
        logger.debug("Json answer is created -> \n" + jsonAnswer);


        logger.info("[Global Chat]" + message.getNickname() + ": " + message.getMessage());
        for (Map.Entry<WebSocket,Client> pair : clientHashMap.entrySet()) { // Берем всех клиентов
            WebSocket socket = pair.getValue().getWebSocket(); // Берем у каждого клиента сокет
            socket.send(jsonAnswer); // Отправляем сообщение
        }
    }
}
