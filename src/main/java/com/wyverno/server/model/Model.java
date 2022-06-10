package com.wyverno.server.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class Model {

    /*private static final Logger logger = LoggerFactory.getLogger(Model.class);
    //private ArrayList<View> views;
    //private final Server server;
    private Thread serverThread;

    public Model() {
        this(4747); // Обращаемся к собственому конструктору с аргументом со стандартным портом
    }

    public Model(int port) {
        //this.views = new ArrayList<>(); // Создаем лист вюшек
        //this.server = new Server(port); // Создаем сервер
        logger.trace("Create server");
        logger.info("Server is starting now");
    }

    *//*public void addView(View view) {
        views.add(view);
    }*//*

    public void startServer() {
        logger.trace("Create server thread");
        this.serverThread = new Thread(this.server); // Создаем поток в аргументы которого закидуем Сервер который реализует интерфейс Runnable
        this.serverThread.start(); // Запускаем поток
        logger.trace("Thread server is start now");
    }

    public void close() throws InterruptedException {
        this.server.stop();
        this.serverThread.interrupt();
    }*/
}
