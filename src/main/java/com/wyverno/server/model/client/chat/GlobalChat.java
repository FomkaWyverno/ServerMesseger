package com.wyverno.server.model.client.chat;

public class GlobalChat extends Chat {
    private static final String DEFAULT_NAME = "Global-Chat";

    public GlobalChat(String nameChat, int maxMessages) {
        super(nameChat, maxMessages);
    }

    public GlobalChat(String nameChat) {
        super(nameChat);
    }

    public GlobalChat(int maxMessages) {
        this(DEFAULT_NAME,maxMessages);
    }

    public GlobalChat() {
        this(DEFAULT_NAME);
    }
}
