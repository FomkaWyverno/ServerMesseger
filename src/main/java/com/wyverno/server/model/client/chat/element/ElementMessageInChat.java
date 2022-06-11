package com.wyverno.server.model.client.chat.element;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wyverno.server.model.ParserJSON;

public abstract class ElementMessageInChat implements ParserJSON {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    private int id;

    private final String elementName = this.getClass().getSimpleName();

    public ElementMessageInChat(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getElementName() {
        return elementName;
    }

    @Override
    public abstract String toJSON() throws JsonProcessingException;
}
