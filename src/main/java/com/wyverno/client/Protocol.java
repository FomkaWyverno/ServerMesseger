package com.wyverno.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class Protocol {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static String getJsonMessage(User user) throws JsonProcessingException {
        JsonNode jsonNode = objectMapper.readTree(objectMapper.writeValueAsString(user));
        setType(jsonNode,"nickname");
        return jsonNode.toString();
    }

    public static String getJsonMessage(Message message) throws JsonProcessingException {
        JsonNode jsonNode = objectMapper.readTree(objectMapper.writeValueAsString(message));
        setType(jsonNode,"message");
        return jsonNode.toString();
    }

    private static void setType(JsonNode jsonNode,String type) {
        ObjectNode node = (ObjectNode) jsonNode;
        node.put("type",type);
    }
}
