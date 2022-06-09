package com.wyverno.server;


import com.wyverno.server.controller.Controller;
import com.wyverno.server.model.Model;
import com.wyverno.server.view.View;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class Main {

    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) throws InterruptedException {
        logger.info("Starting...");
        Model model = new Model(50); // Создаем модель и указываем порт
        logger.trace("Create Model");
        Controller controller = new Controller(model);
        logger.trace("Create controller");
        View view = new View(controller);
        logger.trace("Create view");
        model.addView(view);
        logger.trace("Model adding view");

        model.startServer();
    }
}
