package com.wyverno.server.model.client;

import org.java_websocket.WebSocket;

public class Client {
    private String nickname;
    private WebSocket webSocket;

    public Client(String nickname, WebSocket webSocket) {
        this.nickname = nickname;
        this.webSocket = webSocket;
    }
}
