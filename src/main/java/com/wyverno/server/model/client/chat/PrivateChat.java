package com.wyverno.server.model.client.chat;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.wyverno.server.model.Server;
import com.wyverno.server.model.client.Session;

public class PrivateChat extends Chat {

    private static int countID = 0;

    private final int id = countID++;

    private boolean hasPassword;

    @JsonIgnore
    private String password;
    @JsonIgnore
    private Session mainClient;
    @JsonIgnore
    private Server observer;
    @JsonIgnore
    private boolean isNeedObservable = true;


    public PrivateChat(String nameChat, int maxMessages, String password, Session client, Server observer) {
        super(nameChat,maxMessages);
        this.joinClient(client);
        this.password = password;
        this.mainClient = client;
        this.hasPassword = true;
        this.observer = observer;
    }

    public PrivateChat(String nameChat, int maxMessages, Session client, Server observer) {
        super(nameChat, maxMessages);
        this.joinClient(client);
        this.mainClient = client;
        this.hasPassword = false;
        this.observer = observer;
    }

    public PrivateChat(String nameChat, String password, Session client, Server observer) {
        super(nameChat);
        this.joinClient(client);
        this.mainClient = client;
        this.password = password;
        this.hasPassword = true;
        this.observer = observer;
    }

    public PrivateChat(String nameChat, int maxMessages, String password, Server observer) {
        super(nameChat,maxMessages);
        this.password = password;
        this.hasPassword = true;
        this.observer = observer;
    }

    public PrivateChat(String nameChat, int maxMessages, Server observer) {
        super(nameChat, maxMessages);
        this.hasPassword = false;
        this.observer = observer;
    }

    public PrivateChat(String nameChat, String password, Server observer) {
        super(nameChat);
        this.password = password;
        this.hasPassword = true;
        this.observer = observer;
    }

    public PrivateChat(String nameChat, Server observer) {
        super(nameChat);
        this.hasPassword = false;
        this.observer = observer;
    }

    public PrivateChat(String nameChat, Session client, Server observer) {
        super(nameChat);
        this.hasPassword = false;
        this.joinClient(client);
        this.mainClient = client;
        this.observer = observer;
    }

    public boolean joinClient(Session client, String password) { // Дополняем реализацию входа

        if (!this.hasPassword || this.password.equals(password)) {
            super.joinClient(client);
            return true;
        }
        return false;
    }

    @Override
    public void leaveClient(Session client) {
        logger.trace("Client leave from private chat -> " + this + ": " + client.getNickname());
        super.leaveClient(client);
        if (isNeedObservable) {
            logger.debug("Is need observable chat");
            this.observer.update(this);
        } else {
            logger.debug("is not need observable chat");
        }
    }

    public int getId() {
        return id;
    }

    public boolean isHasPassword() {
        return hasPassword;
    }

    public void setHasPassword(boolean hasPassword) {
        this.hasPassword = hasPassword;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Session getMainClient() {
        return mainClient;
    }

    public void setMainClient(Session mainClient) {
        this.mainClient = mainClient;
    }

    public Server getObserver() {
        return observer;
    }

    public void setObserver(Server observer) {
        this.observer = observer;
    }

    public void setNeedObservable(boolean isNeedClose) {
        this.isNeedObservable = isNeedClose;
    }
}
