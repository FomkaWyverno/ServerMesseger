package com.wyverno.server.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wyverno.server.model.client.Client;
import com.wyverno.server.model.client.chat.Chat;
import com.wyverno.server.model.client.chat.Message;
import org.java_websocket.WebSocket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class Events {

    private static final Logger logger = LoggerFactory.getLogger(Events.class);
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public synchronized static void joinNewClient(JsonNode jsonNode,
                                                  WebSocket webSocketClient,
                                                  int requestID,
                                                  HashMap<WebSocket,Client> clientHashMap,
                                                  Chat joinChat) throws JsonProcessingException {
        Client client = new Client(jsonNode.get("nickname").asText(),webSocketClient); // Создаем нового пользователя сервера


        // Код 0 все отлично
        // Код 1 Ник занят
        if (isFreeNickname(client,clientHashMap)) {
            logger.debug("User chose a free name");
            clientHashMap.put(webSocketClient,client); // Кладем его в мапу под ключем сокета
            logger.debug("Put in hashMap new client | HashMap -> " + clientHashMap.toString());
            joinChat.joinClient(client);


            Response response = new Response(requestID,0,"OK",Response.Type.authorization);
            String responseJson = response.toJSON();
            logger.debug("Json response -> " + responseJson);
            webSocketClient.send(responseJson);
        } else {
            logger.debug("The user has chosen a non-free name");

            Response response = new Response(requestID,1,"Никнейм занят!",Response.Type.authorization);
            String responseJson = response.toJSON();
            logger.debug("Json response -> " + responseJson);
            webSocketClient.send(responseJson);
        }
        logger.trace("Send to server response");

    }

    public synchronized static void sendMessage(JsonNode jsonNode,
                                                WebSocket webSocketClient,
                                                int requestID,
                                                HashMap<WebSocket, Client> clientHashMap) throws JsonProcessingException { // Пользователь отправил сообщение
        Client client = clientHashMap.get(webSocketClient); // Берем у Сокета обьект Клиента
        logger.debug("HashMap ->" + clientHashMap.toString()); // Показываем внутреность карты
        logger.trace("Get client object from clientHashMap: " + client.toString()); // Оповещаем что мы взяли из карты клиента
        logger.trace("We have a json node -> " + jsonNode.textValue());

        Chat chat = client.getRightNowChat();

        chat.sendMessage(client,jsonNode.get("message").asText());

        /*String nickname = client.getNickname(); // Узнаем никнейм
        logger.trace("Get client nickname: " + nickname); // Логгируем никнейм

        String messageClient = jsonNode.get("message").asText(); // Берем у запроса сообщение
        logger.trace("Get client message: " + messageClient); // Логируем сообщение


        Message message = new Message(nickname,messageClient); // Создаем обьект сообщение который включает в себе никнейм и сообщение
        logger.trace("Created message object");


        logger.info("[Global Chat] " + message.getNickname() + ": " + message.getMessage());

        Response response = new Response(requestID,0,objectMapper.writeValueAsString(message),Response.Type.message); // Создаем Response для клиента
        String jsonResponse = response.toJSON();

        for (Map.Entry<WebSocket,Client> pair : clientHashMap.entrySet()) { // Берем всех клиентов
            WebSocket socket = pair.getValue().getWebSocket(); // Берем у каждого клиента сокет
            socket.send(jsonResponse); // Отправляем сообщение
        }*/
    }

    private synchronized static boolean isFreeNickname(Client client, HashMap<WebSocket,Client> clientHashMap) { // Проверка свободный ли никнейм на сервере
        boolean isFree = true;
        for (Map.Entry<WebSocket, Client> pair : clientHashMap.entrySet()) {
            if (pair.getValue().getNickname().equals(client.getNickname())) {
                isFree = false;
                break;
            }
        }
        return isFree;
    }
}
