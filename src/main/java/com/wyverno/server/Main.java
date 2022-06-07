package com.wyverno.server;


import com.wyverno.server.controller.Controller;
import com.wyverno.server.model.Model;
import com.wyverno.server.view.View;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        Model model = new Model(50);
        Controller controller = new Controller(model);
        View view = new View(controller);
        model.addView(view);

    }
}
