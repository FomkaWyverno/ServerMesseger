package com.wyverno.server.model.sql;

import com.wyverno.server.model.client.chat.account.Account;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
        logger.debug("Try connecting to Database");
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

        logger.info("Connected to Database!");
    }

    public DataBase(Properties properties) throws SQLException, IOException {
        this(properties.getProperty("url"),
             properties.getProperty("username"),
             properties.getProperty("password"),
             DataBase.Type.valueOf(properties.getProperty("type")));

    }

    public List<String> getFriendsUsernameByID_User(int id) throws SQLException {

        logger.trace("Getting friends from user by id user");

        String sql = String.format("SELECT `username` FROM `accounts` JOIN `friends` ON `friends`.`id_friend` = `accounts`.`id` WHERE `friends`.`id_user` = %d;", id);


        logger.debug("SQL Execute: " + sql);

        ResultSet set = statement.executeQuery(sql);

        List<String> list = new ArrayList<>();

        while (set.next()) {
            list.add(set.getString("username"));
        }

        logger.debug("List friends -> " + list);

        return list;
    }

    public int isLoggedAndGetID(String username, String password) { // return -1 если имя или пароль не соотвествует параметру
        try {
            String sql = String.format(
                    "SELECT `id` FROM `accounts` WHERE `username` = '%s' AND `password` = '%s';",
                    username,password);
            ResultSet resultSet = statement.executeQuery(sql);
            if (resultSet.next()) {
                return resultSet.getInt("id");
            }
        } catch (SQLException e) {
            logger.error(e.getMessage());
        }
        return -1;
    }

    public enum Type {
        MySQL, PostgreSQL
    }
}
