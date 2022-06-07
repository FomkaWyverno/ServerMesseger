package com.wyverno.server.view;

import com.wyverno.server.controller.Controller;

import java.io.IOException;
import java.util.ArrayList;

public class View {

    private Controller controller;
    private ArrayList<Client> clients;



    public View(Controller controller) {
        this.controller = controller;
    }

    public void update(String message) throws IOException {
        for (Client client : clients) {
            client.sendMessage(message);
        }
    }
}
