package com.wyverno.server.model.client.chat.element;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Message extends ElementMessageInChat {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    private String nickname;
    private String message;
    private int id;

    public Message(String nickname, String message, int id) {
        super(id);
        this.nickname = nickname;
        this.message = message;
        this.id = id;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override()
    public String toString() {
        return this.nickname + ": " + this.message;
    }

    @Override
    public String toJSON() throws JsonProcessingException {
        return objectMapper.writeValueAsString(this);
    }
}
