package com.wyverno.server.model.events.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.wyverno.server.model.Server;
import com.wyverno.server.model.client.Client;
import com.wyverno.server.model.client.chat.PrivateChat;
import com.wyverno.server.model.events.AbstractEvent;
import com.wyverno.server.model.response.Response;
import org.java_websocket.WebSocket;

import java.util.HashMap;

public class TryCreateChatEvent extends AbstractEvent {

    public TryCreateChatEvent(JsonNode jsonNode, WebSocket webSocket, int requestID, Server server) {
        super(jsonNode, webSocket, requestID, server);
    }

    @Override
    public void runEvent() {
        super.runEvent();

        Response response;

        if (isFreeChatName(jsonNode.get("name").asText())) {

            HashMap<WebSocket, Client> clientHashMap = this.server.getClientHashMap();

            Client client = clientHashMap.get(this.webSocket);

            PrivateChat chat;

            if (this.jsonNode.get("password").asText().isEmpty()) {
                this.logger.debug("Client try create chat without password");
                chat = new PrivateChat(jsonNode.get("name").asText(), client, server);
            } else {
                this.logger.debug("Client try create chat with password");
                chat = new PrivateChat(jsonNode.get("name").asText(),jsonNode.get("password").asText(), client, this.server);
            }
            this.server.addChat(chat);

            response = new Response(this.requestID,0, "OK",Response.Type.tryCreateChat);
        } else {
            this.logger.trace("Create response for client what chat name is not free!");
            response = new Response(this.requestID,1,"Not Free Name Chat",Response.Type.tryCreateChat);
        }

        try {
            String jsonResponse = response.toJSON();
            this.logger.debug("Sending response to websocket client -> " + jsonResponse);

            this.webSocket.send(jsonResponse); // Отправляем отклик клиенту
        } catch (JsonProcessingException e) { // Если произошла ошибка в парсинге в JSON
            this.logger.error("JSON Processing Exception in Try Create Chat Event -> " + e.getMessage());
        }


    }

    private synchronized boolean isFreeChatName(String chatName) { // Свободен ли имя чата
        boolean isFree = true;
        chatName = chatName.toLowerCase();
        for (PrivateChat chat : this.server.getChatList()) {
            if (chat.getNameChat().toLowerCase().equals(chatName)) {
                this.logger.info("Chat name is not free.");
                isFree = false;
                break;
            }
        }
        return isFree;
    }
}
