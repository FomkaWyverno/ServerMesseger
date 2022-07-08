package com.wyverno.server.model.events.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.wyverno.server.model.Server;
import com.wyverno.server.model.client.Session;
import com.wyverno.server.model.client.chat.Chat;
import com.wyverno.server.model.events.AbstractEvent;
import org.java_websocket.WebSocket;

import java.util.HashMap;

public class SendMessageEvent extends AbstractEvent { // Отправка сообщения в чат

    public SendMessageEvent(JsonNode jsonNode, WebSocket webSocket, int requestID, Server server) {
        super(jsonNode, webSocket, requestID, server);
    }

    @Override
    public void runEvent() {
        super.runEvent();

        HashMap<WebSocket, Session> clientHashMap = this.server.getClientHashMap();

        Session client = clientHashMap.get(this.webSocket); // Берем у Сокета обьект Клиента
        logger.debug("HashMap ->" + clientHashMap.toString()); // Показываем внутреность карты
        logger.trace("Get client object from clientHashMap: " + client.toString()); // Оповещаем что мы взяли из карты клиента
        logger.trace("We have a json node -> " + jsonNode.textValue());

        Chat chat = client.getRightNowChat();

        chat.sendMessage(client,jsonNode.get("message").asText());
    }
}
