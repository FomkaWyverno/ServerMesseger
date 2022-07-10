package com.wyverno.server.model.client;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wyverno.server.model.ParserJSON;
import com.wyverno.server.model.client.chat.Chat;
import com.wyverno.server.model.client.chat.account.Account;
import org.java_websocket.WebSocket;

public class Client implements ParserJSON {

    @JsonIgnore
    private static final ObjectMapper objectMapper = new ObjectMapper();

    private final Account account;


    @JsonIgnore
    private WebSocket webSocket;
    @JsonIgnore
    private Chat rightNowChat;

    public Client(Account account, WebSocket webSocket) {
        this.account = account;
        this.webSocket = webSocket;
    }

    public String getNickname() {
        return this.account.getUsername();
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
        return this.account.getId();
    }

    @Override
    public String toString() {
        return "Client{" +
                "account=" + account +
                ", webSocket=" + webSocket +
                ", rightNowChat=" + rightNowChat +
                '}';
    }

    @Override
    public String toJSON() throws JsonProcessingException {
        return objectMapper.writeValueAsString(this);
    }
}
