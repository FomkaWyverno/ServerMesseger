package com.wyverno.server;

import com.wyverno.server.model.Server;
import com.wyverno.server.model.client.chat.GlobalChat;
import com.wyverno.server.model.sql.DataBase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Properties;

public class Main {

    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {

        Server server = new Server(50,new GlobalChat(100));

        logger.trace("Created server");
        Thread serverThread = new Thread(server);
        logger.trace("Created Thread for Server");

        serverThread.start();
        logger.trace("Thread Server is start");

        try {

            Properties properties = new Properties();
            properties.load(new FileInputStream("./database.properties"));

            DataBase dataBase = new DataBase(properties);

            logger.info("Connected to Database!");
        } catch (SQLException | IOException e) {
            logger.error(e.getMessage());
        }
    }
}
