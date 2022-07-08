package com.wyverno.server.model.events.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.wyverno.server.model.Server;
import com.wyverno.server.model.client.Session;
import com.wyverno.server.model.events.AbstractEvent;
import com.wyverno.server.model.response.Response;
import org.java_websocket.WebSocket;

import java.util.HashMap;
import java.util.Map;

public class AuthorizationEvent extends AbstractEvent { // Авторизация


    public AuthorizationEvent(JsonNode jsonNode, WebSocket webSocket, int requestID, Server server) {
        super(jsonNode, webSocket, requestID, server);
    }

    @Override
    public synchronized void runEvent() {
        super.runEvent();

        HashMap<WebSocket, Session> clientHashMap = this.server.getClientHashMap();


        try {

            Response response;
            if (isFreeNickname(this.jsonNode.get("nickname").asText())) {
                this.logger.debug("User chose a free name"); // Никнейм свободен

                Session client = new Session(this.jsonNode.get("nickname").asText(),this.webSocket); // Создаем клиента

                clientHashMap.put(this.webSocket,client); // Кладем его в мапу под ключем сокета
                this.logger.debug("Put in hashMap new client | HashMap -> " + clientHashMap.toString());
                this.server.GLOBAL_CHAT.joinClient(client); // Подключаем пользователя в глобальный чат


                response = new Response(this.requestID,0, client.toJSON(), Response.Type.authorization); // Создаем отклик для пользователя

            } else {
                this.logger.debug("The user has chosen a non-free name"); // Пользователь выбрал ник который уже занят
                response = new Response(this.requestID,1,"Никнейм занят!",Response.Type.authorization); // Создаем отклик для пользователя
            }

            String responseJson = response.toJSON(); // Преобразовуем отклик в JSON
            this.logger.debug("Json response -> " + responseJson); // Выводим в дебаге
            this.webSocket.send(responseJson); // Отправляем пользователю отклик
            this.logger.trace("Send to server response");


        } catch (JsonProcessingException e) {
            this.logger.error("Json Parsing error -> " + e.getMessage()); // В случае ошибки парсинга JSON отправляем сообщение о ошибке
        }


    }

    private synchronized boolean isFreeNickname(String clientNickname) { // Проверка свободный ли никнейм на сервере
        boolean isFree = true;

        HashMap<WebSocket, Session> clientHashMap = this.server.getClientHashMap();


        clientNickname = clientNickname.toLowerCase();
        for (Map.Entry<WebSocket, Session> pair : clientHashMap.entrySet()) {
            if (pair.getValue().getNickname().toLowerCase().equals(clientNickname)) {
                isFree = false;
                break;
            }
        }
        return isFree;
    }

}
