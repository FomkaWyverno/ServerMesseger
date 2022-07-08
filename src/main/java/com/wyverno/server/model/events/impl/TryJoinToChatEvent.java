package com.wyverno.server.model.events.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.wyverno.server.model.Server;
import com.wyverno.server.model.client.Session;
import com.wyverno.server.model.client.chat.PrivateChat;
import com.wyverno.server.model.events.AbstractEvent;
import com.wyverno.server.model.response.Response;
import com.wyverno.server.model.response.ResponseDataTryJoinToChatError;
import org.java_websocket.WebSocket;

import java.util.HashMap;

public class TryJoinToChatEvent extends AbstractEvent { // Попытка подключится к приватному чату

    public TryJoinToChatEvent(JsonNode jsonNode, WebSocket webSocket, int requestID, Server server) {
        super(jsonNode, webSocket, requestID, server);
    }

    @Override
    public void runEvent() {
        super.runEvent();

        HashMap<WebSocket, Session> clientHashMap = this.server.getClientHashMap();

        Session client = clientHashMap.get(this.webSocket);
        PrivateChat chat = null;
        this.logger.trace("Start search chat");
        for (PrivateChat c : this.server.getChatList()) { // Ищем чат по айди
            if (c.getId() == this.jsonNode.get("chatID").asInt()) { // Если айди чата равен айди запроса
                this.logger.debug("Will find chat by id");
                chat = c; // Устанавливаем чат в переменную
                break;
            }
        }

        try {
            Response response;

            if (chat == null) { // Если чат не был найден
                this.logger.debug("Not found chat by id");
                response = new Response(this.requestID, 3,
                        new ResponseDataTryJoinToChatError(this.jsonNode.get("chatID").asInt(), "Does not exits").toJSON(),
                        Response.Type.tryJoinToChat);
            } else if (chat.joinClient(client, this.jsonNode.get("password").asText())) { // Зашел ли пользователь в чат?
                response = new Response(this.requestID, 0, "OK", Response.Type.tryJoinToChat);
            } else {
                response = new Response(this.requestID, 1,
                        new ResponseDataTryJoinToChatError(chat.getId(), "Bad password").toJSON(),
                        Response.Type.tryJoinToChat);
            }

            String jsonResponse = response.toJSON();

            this.logger.debug("Response to JSON ->  " + jsonResponse);
            this.webSocket.send(jsonResponse);
        } catch (JsonProcessingException e) {
            this.logger.error("JSON Processing Exception in Try Join To Chat Event -> " + e.getMessage());
        }
    }
}
