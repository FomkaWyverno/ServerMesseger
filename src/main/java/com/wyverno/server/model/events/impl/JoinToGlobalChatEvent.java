package com.wyverno.server.model.events.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.wyverno.server.model.Server;
import com.wyverno.server.model.client.Client;
import com.wyverno.server.model.events.AbstractEvent;
import org.java_websocket.WebSocket;

public class JoinToGlobalChatEvent extends AbstractEvent {

    public JoinToGlobalChatEvent(JsonNode jsonNode, WebSocket webSocket, int requestID, Server server) {
        super(jsonNode, webSocket, requestID, server);
    }

    @Override
    public void runEvent() {
        super.runEvent();

        Client client = this.server.getClientHashMap().get(this.webSocket);

        this.server.GLOBAL_CHAT.joinClient(client);
    }
}
