package com.wyverno.server.model;

import com.wyverno.server.view.View;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

public class Model {

    private static final Logger logger = LoggerFactory.getLogger(Model.class);
    private ArrayList<View> views;
    private final Server server;

    public Model() {
        this(4747);
    }

    public Model(int port) {
        this.views = new ArrayList<>();
        this.server = new Server(port);
        logger.trace("Create server");
        logger.info("Server is starting now");
        this.server.run();
    }

    public void addView(View view) {
        views.add(view);
    }

    public void close() throws InterruptedException {
        server.stop();
    }
}
