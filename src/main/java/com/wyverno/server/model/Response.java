package com.wyverno.server.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Response {
    private static final Logger logger = LoggerFactory.getLogger(Response.class);
    private static final ObjectMapper objectMapper = new ObjectMapper();

    private int requestID;
    private int code;
    private Type type;
    private String data;


    public Response(int requestID, int code, String response, Type type) {
        logger.trace("Create response");
        this.requestID = requestID;
        this.code = code;
        this.type = type;
        this.data = response;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public int getRequestID() {
        return requestID;
    }

    public void setRequestID(int requestID) {
        this.requestID = requestID;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public String toJSON() throws JsonProcessingException {
        return objectMapper.writeValueAsString(this);
    }

    public enum Type {
        authorization, joinToChat, leaveFromChat, message, deleteMessage, listMessages;
    }
}
