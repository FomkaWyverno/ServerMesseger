package com.wyverno.server.model.client.chat;

import com.wyverno.server.model.client.Client;

import java.util.List;

public class PrivateChat extends Chat {

    public PrivateChat(String nameChat, int maxMessages, List<Client> chatClients) {
        super(nameChat, maxMessages, chatClients);
    }

    public PrivateChat(String nameChat, int maxMessages) {
        super(nameChat, maxMessages);
    }

    public PrivateChat(String nameChat) {
        super(nameChat);
    }
}
