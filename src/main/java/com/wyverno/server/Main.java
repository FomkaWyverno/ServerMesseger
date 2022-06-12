package com.wyverno.server;

import com.wyverno.server.model.Server;
import com.wyverno.server.model.client.chat.GlobalChat;
import com.wyverno.server.model.client.chat.PrivateChat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {

    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) throws InterruptedException {
//        logger.info("Starting...");
//        Model model = new Model(50); // Создаем модель и указываем порт
//        logger.trace("Create Model");
        /*Controller controller = new Controller(model);
        logger.trace("Create controller");
        View view = new View(controller);
        logger.trace("Create view");
        model.addView(view);
        logger.trace("Model adding view");*/
        //model.startServer();

        Server server = new Server(50,new GlobalChat(10));

        server.addChat(new PrivateChat("Test-1",10,"123",server));
        server.addChat(new PrivateChat("ATest-2",10,server));
        server.addChat(new PrivateChat("BTest-3", 10,server));
        server.addChat(new PrivateChat("BTG","string",server));

        logger.trace("Created server");
        Thread serverThread = new Thread(server);
        logger.trace("Created Thread for Server");

        serverThread.start();
        logger.trace("Thread Server is start");

    }
}
