package com.wyverno.server.model;

import com.fasterxml.jackson.core.JsonProcessingException;

public interface ParserJSON {
    String toJSON() throws JsonProcessingException;
}
