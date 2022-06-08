package com.wyverno.client;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft;
import org.java_websocket.handshake.ServerHandshake;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.concurrent.TimeUnit;


public class Client extends WebSocketClient {

    private static final Logger logger = LoggerFactory.getLogger(Client.class);

    public static void main(String[] args) throws URISyntaxException, InterruptedException {
        WebSocketClient client = new Client("ws://localhost:50");

        client.connect();
        logger.info("Connect to server");
        if(!client.isOpen()) {
            Thread.sleep(TimeUnit.SECONDS.toMillis(1));
        }
    }

    public Client(URI serverUri, Draft draft) {
        super(serverUri, draft);
    }

    public Client(URI serverURI) {
        super(serverURI);
    }

    public Client(String URI) throws URISyntaxException {
        super(new URI(URI));
    }



    @Override
    public void onOpen(ServerHandshake serverHandshake) {

    }

    @Override
    public void onMessage(String s) {

    }

    @Override
    public void onClose(int i, String s, boolean b) {

    }

    @Override
    public void onError(Exception e) {

    }
}
