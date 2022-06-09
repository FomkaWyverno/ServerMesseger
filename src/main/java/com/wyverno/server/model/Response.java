package com.wyverno.server.model;

public class Response {
    private int requestID;
    private int code;
    private String type;
    private String response;

    public Response(int requestID ,int code, String response) {
        this.requestID = requestID;
        this.code = code;
        this.response = response;
    }

    public Response(int requestID, int code, String response, String type) {
        this.requestID = requestID;
        this.code = code;
        this.type = type;
        this.response = response;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public int getRequestID() {
        return requestID;
    }

    public void setRequestID(int requestID) {
        this.requestID = requestID;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
