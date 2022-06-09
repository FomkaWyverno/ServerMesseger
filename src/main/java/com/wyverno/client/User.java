package com.wyverno.client;

import java.io.IOException;
import java.net.URISyntaxException;

public class User {
    private String nick;

    public User(String userNickname) {
        this.nick = userNickname;
    }

    public String getNick() {
        return nick;
    }

    public static void main(String[] args) throws URISyntaxException, IOException, InterruptedException {
        Client.main(null);
    }
}
