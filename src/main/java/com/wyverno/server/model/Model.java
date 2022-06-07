package com.wyverno.server.model;

import com.wyverno.server.view.View;

import java.io.IOException;
import java.util.ArrayList;

public class Model {

    private ArrayList<View> views;
    private final Server SERVER;

    public Model() throws IOException {
        this(4747);
    }

    public Model(int port) throws IOException {
        this.views = new ArrayList<>();
        this.SERVER = new Server(port);
    }

    public void addView(View view) {
        views.add(view);
    }

    public void close() throws IOException {
        SERVER.close();
    }
}
