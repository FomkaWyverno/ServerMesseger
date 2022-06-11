package com.wyverno.server.model.client.chat;

import com.wyverno.server.model.client.Client;

public class PrivateChat extends Chat {

    public PrivateChat(String nameChat, int maxMessages, Client client) {
        super(nameChat, maxMessages);
        this.joinClient(client);
    }

    public PrivateChat(String nameChat, int maxMessages) {
        super(nameChat, maxMessages);
    }

    public PrivateChat(String nameChat) {
        super(nameChat);
    }
}
