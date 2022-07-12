package com.wyverno.server.model.client.chat.account;

public class AccountCreateUsernameIsExists extends Exception {
    public AccountCreateUsernameIsExists(String message) {
        super(message);
    }
}
