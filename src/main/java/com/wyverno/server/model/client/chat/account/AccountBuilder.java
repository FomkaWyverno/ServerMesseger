package com.wyverno.server.model.client.chat.account;

import com.wyverno.server.model.sql.DataBase;

import java.sql.SQLException;

public class AccountBuilder {

    private final DataBase dataBase;
    private String username;
    private String password;

    public AccountBuilder(DataBase dataBase) {
        this.dataBase = dataBase;
    }

    public AccountBuilder username(String username) {
        this.username = username;
        return this;
    }

    public AccountBuilder password(String password) {
        this.password = password;
        return this;
    }

    public Account login() throws AccountIsNotLogged {

        int id = this.dataBase.isLoggedAndGetID(this.username,this.password);

        if (id == -1) { // Если пароль или имя не верные отправляем ошибку
            throw new AccountIsNotLogged();
        } else {
            return new Account(id, this.username, this.password);
        }
    }
}
