package com.wyverno.server.model.client.chat;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wyverno.server.model.ParserJSON;

public abstract class ElementMessageInChat implements ParserJSON {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    private int id;

    public ElementMessageInChat(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public abstract String toJSON() throws JsonProcessingException;
}
