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


        /*PrivateChat testChat1 = new PrivateChat("Test1",10,"str",server);
        PrivateChat testChat2 = new PrivateChat("ETO-Test2",10,server);
        PrivateChat testChat3 = new PrivateChat("Proverka",10,server);
        PrivateChat testChat4 = new PrivateChat("ChatHavePassword", 10,"pass",server);
        testChat1.setNeedObservable(false);
        testChat2.setNeedObservable(false);
        testChat3.setNeedObservable(false);
        testChat4.setNeedObservable(false);


        server.addChat(testChat1);
        server.addChat(testChat2);
        server.addChat(testChat3);
        server.addChat(testChat4);*/
    }
}
