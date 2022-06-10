package com.wyverno.server.model.client.chat;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ConnectDisconnectElement extends ElementMessageInChat {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    private String name;
    private boolean isConnect;

    public ConnectDisconnectElement(int id, String name, boolean isConnect) {
        super(id);
        this.name = name;
        this.isConnect = isConnect;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isConnect() {
        return isConnect;
    }

    public void setConnect(boolean connect) {
        isConnect = connect;
    }

    @Override
    public String toJSON() throws JsonProcessingException {
        return objectMapper.writeValueAsString(this);
    }
}
