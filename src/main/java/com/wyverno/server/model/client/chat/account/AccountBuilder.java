package com.wyverno.server.model.client.chat.account;

import com.wyverno.server.model.sql.DataBase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AccountBuilder {

    private final static Logger logger = LoggerFactory.getLogger(AccountBuilder.class);

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

        int id = this.dataBase.getIdAccount(this.username,this.password);

        if (id == -1) { // Если пароль или имя не верные отправляем ошибку
            throw new AccountIsNotLogged();
        } else {
            return new Account(id, this.username, this.password);
        }
    }

    public Account registration() throws AccountCreateUsernameIsExists {

        logger.debug("Try create account");
        boolean isCreate = this.dataBase.insertNewAccount(this.username, this.password);
        logger.debug(String.format("Is Create Account? -> %b username = %s and password = %s",isCreate, this.username, this.password));

        if (!isCreate) {
            logger.info("Account not registered cause -> Username is not free");
            throw new AccountCreateUsernameIsExists("Username is not free!");
        }

        int id = this.dataBase.getIdAccount(this.username, this.password);

        return new Account(id, this.username, this.password);
    }
}
