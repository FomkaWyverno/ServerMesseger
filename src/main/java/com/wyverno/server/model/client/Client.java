package com.wyverno.server.model.client;

import com.wyverno.server.model.client.chat.Chat;
import org.java_websocket.WebSocket;

public class Client {
    private String nickname;
    private WebSocket webSocket;
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

    @Override
    public String toString() {
        return "Client{" +
                "nickname='" + nickname + '\'' +
                ", webSocket=" + webSocket +
                '}';
    }
}
