package com.wyverno.server.model.client;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wyverno.server.model.ParserJSON;
import com.wyverno.server.model.client.chat.Chat;
import org.java_websocket.WebSocket;

public class Client implements ParserJSON {
    @JsonIgnore
    private static int UID_COUNTER = 0;
    @JsonIgnore
    private static final ObjectMapper objectMapper = new ObjectMapper();


    private final int UID = UID_COUNTER++;
    private String nickname;


    @JsonIgnore
    private WebSocket webSocket;
    @JsonIgnore
    private Chat rightNowChat;

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

    public Chat getRightNowChat() {
        return rightNowChat;
    }

    public void setRightNowChat(Chat rightNowChat) {
        this.rightNowChat = rightNowChat;
    }

    public int getUID() {
        return UID;
    }

    @Override
    public String toString() {
        return "Client{" +
                "nickname='" + nickname + '\'' +
                ", webSocket=" + webSocket +
                '}';
    }

    @Override
    public String toJSON() throws JsonProcessingException {
        return objectMapper.writeValueAsString(this);
    }
}
