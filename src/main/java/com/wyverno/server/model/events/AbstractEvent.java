package com.wyverno.server.model.events;

import com.fasterxml.jackson.databind.JsonNode;
import com.wyverno.server.model.Server;
import org.java_websocket.WebSocket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractEvent {

    protected final Logger logger = LoggerFactory.getLogger(AbstractEvent.class);
    protected final Server server;
        protected final JsonNode jsonNode;
        protected final WebSocket webSocket;
        protected final int requestID;

    public AbstractEvent(JsonNode jsonNode, WebSocket webSocket, int requestID, Server server) {
        this.server = server;
        this.jsonNode = jsonNode;
        this.webSocket = webSocket;
        this.requestID = requestID;
    }

    public synchronized void runEvent() {
        logger.debug("Type is " + this.getClass().getSimpleName());
    }
}
