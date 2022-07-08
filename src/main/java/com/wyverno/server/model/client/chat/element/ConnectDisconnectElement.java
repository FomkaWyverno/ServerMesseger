package com.wyverno.server.model.client.chat.element;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wyverno.server.model.client.Session;

public class ConnectDisconnectElement extends ElementMessageInChat {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    private boolean isConnect;

    public ConnectDisconnectElement(int id, Session client, boolean isConnect) {
        super(id, client);
        this.isConnect = isConnect;
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
