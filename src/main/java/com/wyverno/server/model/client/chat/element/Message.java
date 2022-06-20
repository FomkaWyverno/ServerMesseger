package com.wyverno.server.model.client.chat.element;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wyverno.server.model.client.Client;

public class Message extends ElementMessageInChat {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    private String message;

    public Message(int id, Client client, String message) {
        super(id, client);
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override()
    public String toString() {
        return this.client.getNickname() + ": " + this.message;
    }

    @Override
    public String toJSON() throws JsonProcessingException {
        return objectMapper.writeValueAsString(this);
    }
}
