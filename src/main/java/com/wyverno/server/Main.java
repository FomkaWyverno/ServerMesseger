package com.wyverno.server;

import com.wyverno.server.model.Server;
import com.wyverno.server.model.client.chat.GlobalChat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {

    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {

        Server server = new Server(50,new GlobalChat(10));

        logger.trace("Created server");
        Thread serverThread = new Thread(server);
        logger.trace("Created Thread for Server");

        serverThread.start();
        logger.trace("Thread Server is start");

        /*Server server = new Server(new GlobalChat());
        Method[] methods = Events.class.getDeclaredMethods();

        for (Method method : methods) {
            if (method.isAnnotationPresent(Event.class)) {

            }
        }*/

    }
}
