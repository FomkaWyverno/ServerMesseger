package com.wyverno.server.model.sql;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class DataBase {

    private final Logger logger = LoggerFactory.getLogger(DataBase.class);

    private final Connection connection;
    private final Statement statement;

    public DataBase(String urlDatabase, String username, String password, Type typeDataBase) throws SQLException {
        StringBuilder url = new StringBuilder(urlDatabase);

        switch (typeDataBase) {
            case MySQL: {
                try {
                    Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
                    url.insert(0,"jdbc:mysql://");
                } catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
                    logger.error(e.getMessage());
                }
                break;
            }
            case PostgreSQL: {
                logger.error("NOT SUPPORT PostgreSQL!!!");
                break;
            }
        }

        this.connection = DriverManager.getConnection(url.toString(), username, password);
        this.statement = this.connection.createStatement();

    }

    public DataBase(Properties properties) throws SQLException, IOException {
        this(properties.getProperty("url"),
             properties.getProperty("username"),
             properties.getProperty("password"),
             DataBase.Type.valueOf(properties.getProperty("type")));

    }

    public List<String> getFriendsUsernameByID_User(int id) throws SQLException {
        ResultSet set = statement.executeQuery(
                String.format(
                        "SELECT `username` FROM `accounts` JOIN `friends` ON `friends`.`id_friend` = `accounts`.`id` WHERE `friends`.`id_user` = %d;", id));

        List<String> list = new ArrayList<>();

        while (set.next()) {
            list.add(set.getString("username"));
        }

        return list;
    }

    public enum Type {
        MySQL, PostgreSQL
    }
}
