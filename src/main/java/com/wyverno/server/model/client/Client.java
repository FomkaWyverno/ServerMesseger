package com.wyverno.server.model.client;

import com.fasterxml.jackson.databind.JsonNode;
import org.java_websocket.WebSocket;

public class Client {
    private String nickname;
    private WebSocket webSocket;

    public Client(String nickname, WebSocket webSocket) {
        this.nickname = nickname;
        this.webSocket = webSocket;
    }

    public String getNickname() {
        return nickname;
    }

    public WebSocket getWebSocket() {
        return webSocket;
    }

    @Override
    public String toString() {
        return "Client{" +
                "nickname='" + nickname + '\'' +
                ", webSocket=" + webSocket +
                '}';
    }
}
