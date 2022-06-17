package com.wyverno.server.model.events.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wyverno.server.model.Server;
import com.wyverno.server.model.client.chat.PrivateChat;
import com.wyverno.server.model.events.AbstractEvent;
import com.wyverno.server.model.response.Response;
import org.java_websocket.WebSocket;

import java.util.List;

public class GetChatListEvent extends AbstractEvent {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public GetChatListEvent(JsonNode jsonNode, WebSocket webSocket, int requestID, Server server) {
        super(jsonNode, webSocket, requestID, server);
    }

    @Override
    public void runEvent() {
        super.runEvent();

        try {
            List<PrivateChat> chatList = this.server.getChatList(); // Берем у сервера чат лист

            this.logger.trace("Get chat list for user -> " + chatList.toString()); // Выводим лист
            String jsonList = objectMapper.writeValueAsString(chatList); // Парсим лист в JSON
            this.logger.debug("List to JSON -> " + jsonList); // Выводим лист в JSON

            Response response = new Response(this.requestID,0,jsonList, Response.Type.gotChatList);
            String responseJSON = response.toJSON(); // Парсим отклик в JSON
            this.logger.debug("Response to JSON -> " + responseJSON);

            this.webSocket.send(responseJSON); // Отправляем отклик пользователю
            this.logger.trace("Send to server response");
        } catch (JsonProcessingException e) {
            this.logger.error("JSON PROCESSING EXCEPTION Message -> " + e.getMessage());
        }
    }
}
