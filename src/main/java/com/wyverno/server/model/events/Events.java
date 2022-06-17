package com.wyverno.server.model.events;

import com.fasterxml.jackson.databind.JsonNode;
import com.wyverno.server.model.Server;
import com.wyverno.server.model.events.impl.*;
import org.java_websocket.WebSocket;

public class Events {

    @Event(type = "authorization")
    public static AbstractEvent authorizationEvent(JsonNode jsonNode, WebSocket webSocket, int requestID, Server server) {
        return new AuthorizationEvent(jsonNode, webSocket, requestID, server);
    }

    @Event(type = "message")
    public static AbstractEvent sendMessageEvent(JsonNode jsonNode, WebSocket webSocket, int requestID, Server server) {
        return new SendMessageEvent(jsonNode, webSocket, requestID, server);
    }

    @Event(type = "getChatList")
    public static AbstractEvent getChatListEvent(JsonNode jsonNode, WebSocket webSocket, int requestID, Server server) {
        return new GetChatListEvent(jsonNode, webSocket, requestID, server);
    }

    @Event(type = "tryJoinToChat")
    public static AbstractEvent tryJoinToChatEvent(JsonNode jsonNode, WebSocket webSocket, int requestID, Server server) {
        return new TryJoinToChatEvent(jsonNode, webSocket, requestID, server);
    }

    @Event(type = "joinToGlobalChat")
    public static AbstractEvent joinToGlobalChatEvent(JsonNode jsonNode, WebSocket webSocket, int requestID, Server server) {
        return new JoinToGlobalChatEvent(jsonNode, webSocket, requestID, server);
    }

    @Event(type = "tryCreateChat")
    public static AbstractEvent tryCreateChatEvent(JsonNode jsonNode, WebSocket webSocket, int requestID, Server server) {
        return new TryCreateChatEvent(jsonNode, webSocket, requestID, server);
    }
}
