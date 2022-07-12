package com.wyverno.server.model.events.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.wyverno.server.model.Server;
import com.wyverno.server.model.client.Client;
import com.wyverno.server.model.client.chat.account.Account;
import com.wyverno.server.model.client.chat.account.AccountBuilder;
import com.wyverno.server.model.client.chat.account.AccountCreateUsernameIsExists;
import com.wyverno.server.model.events.AbstractEvent;
import com.wyverno.server.model.response.Response;
import org.java_websocket.WebSocket;

import java.util.HashMap;

public class RegistrationEvent extends AbstractEvent {

    public RegistrationEvent(JsonNode jsonNode, WebSocket webSocket, int requestID, Server server) {
        super(jsonNode, webSocket, requestID, server);
    }

    @Override
    public synchronized void runEvent() {
        super.runEvent();

        Response response = null;

        String username = this.jsonNode.get("nickname").asText();
        String password = this.jsonNode.get("password").asText();

        try {
            logger.debug(String.format("Try registration account for username = %s and password = %s", username, password));
            Account account = new AccountBuilder(this.server.getDataBase())
                                    .username(username)
                                    .password(password)
                                    .registration();
            logger.info(String.format("Registered a new account under the name = \"%s\"",username));


            Client client = new Client(account, this.webSocket);
            logger.debug("Created client | username = " + username);
            HashMap<WebSocket, Client> clientHashMap = this.server.getClientHashMap();

            clientHashMap.put(this.webSocket, client);
            logger.debug("Add to clientHashMap client | username = " + username);


            this.server.GLOBAL_CHAT.joinClient(client);
            logger.info(String.format("Connected '%s' to GLOBAL CHAT!",username));

            response = new Response(this.requestID, 0, client.toJSON(), Response.Type.registration);

            logger.debug("Created response with code 0 (OK)");
        } catch (AccountCreateUsernameIsExists e) {
            logger.info("User could not register under the name | username = " + username);
            response = new Response(this.requestID, 1, "USERNAME IS EXISTS!", Response.Type.registration);
            logger.debug("Created response with code 1 (USERNAME IS EXISTS!)");
        } catch (JsonProcessingException e) {
            logger.error(e.getMessage());
        }

        assert response != null; // Если ссылка не получила обьекта то отсанавливаем метод

        try {
            String jsonResponse = response.toJSON();
            logger.debug("JSON RESPONSE -> " + jsonResponse);

            this.webSocket.send(jsonResponse);
            logger.debug("Send to WebSocket client! (REGISTRATION)");

        } catch (JsonProcessingException e) {
            logger.error(e.getMessage());
        }

    }
}
