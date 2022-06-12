package com.wyverno.server.model.response;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wyverno.server.model.ParserJSON;



// Нужен для ковпановки информации о попытке входа в чат
public class ResponseDataTryJoinToChatError implements ParserJSON {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    private int chatID;
    private String cause;

    public ResponseDataTryJoinToChatError(int chatID, String cause) {
        this.chatID = chatID;
        this.cause = cause;
    }

    public int getChatID() {
        return chatID;
    }

    public void setChatID(int chatID) {
        this.chatID = chatID;
    }

    public String getCause() {
        return cause;
    }

    public void setCause(String cause) {
        this.cause = cause;
    }

    @Override
    public String toJSON() throws JsonProcessingException {
        return objectMapper.writeValueAsString(this);
    }
}
