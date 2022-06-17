package com.wyverno.server.model.client.chat;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wyverno.server.model.response.Response;
import com.wyverno.server.model.client.Client;
import com.wyverno.server.model.client.chat.element.ConnectDisconnectElement;
import com.wyverno.server.model.client.chat.element.ElementMessageInChat;
import com.wyverno.server.model.client.chat.element.Message;
import org.java_websocket.exceptions.WebsocketNotConnectedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public abstract class Chat {
    protected static final Logger logger = LoggerFactory.getLogger(Chat.class);
    private static final ObjectMapper objectMapper = new ObjectMapper();

    private static final int DEFAULT_MAX_MESSAGES = 60;

    @JsonIgnore
    private volatile LinkedList<ElementMessageInChat> elementMessageInChatLinkedList = new LinkedList<>(); // Элементы чаты
    //private LinkedList<Message> messages = new LinkedList<>();
    @JsonIgnore
    private volatile List<Client> chatClients; // Клиенты в чате
    @JsonIgnore
    private int idElement = 0; // Самый последний айди элемента

    private int maxMessages; // Максимальное количество сообщений
    private String nameChat; // Название чата

    public Chat(String nameChat, int maxMessages, List<Client> chatClients) {
        this.nameChat = nameChat;
        this.maxMessages = maxMessages;
        this.chatClients = chatClients;
        logger.info("Create chat: " + this);
    }

    public Chat(String nameChat, int maxMessages) {
        this(nameChat, maxMessages, new ArrayList<>());
    }

    public Chat(String nameChat) {
        this(nameChat,DEFAULT_MAX_MESSAGES);
    }

    public synchronized void sendMessage(Client client, String message) {
        this.sendMessage(new Message(client.getNickname(),message, idElement));
        this.idElement++;
    }

    public synchronized void sendMessage(Message message) { // Отправляем сообщение
        logger.info(message.toString());
        this.removeLastElementInChat();
        this.addElementInChat(message,Response.Type.message);// Добавляем в начало списка сообщение
    }

    private void removeLastElementInChat() { // Удаляем вышедшие за границы елементы
        while (this.elementMessageInChatLinkedList.size() >= this.maxMessages) { // Проверяем чат на максимальное количество сообщений
            ElementMessageInChat element = removeElementLastInChat();

            try {
                Response response = new Response(-1,0,element.toJSON(),Response.Type.deleteElement);
                this.sendAllClientInChat(response);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }
    }

    private void addElementInChat(ElementMessageInChat element, Response.Type type) {
        this.removeLastElementInChat(); // Удаляем элементы из чата если они выходят границы
        this.elementMessageInChatLinkedList.addFirst(element); // Добавляем в начало списка

        Response response = null;
        try {
            response = new Response(-1,0,element.toJSON(),type);
            logger.debug("Created response for element");
        } catch (JsonProcessingException e) {
            logger.error(e.getMessage() + " Cause -> " + e.getCause());
        }

        assert response != null;

        this.sendAllClientInChat(response); // Создали отклик и отправляем всем участинкам чата
    }

    private ElementMessageInChat removeElementLastInChat() { // Удалить последний элемент из чата
        ElementMessageInChat element = this.elementMessageInChatLinkedList.removeLast(); // Удаляем последний элемент

        if (element instanceof Message) {
            logger.trace("Remove last message in chat [" + this.nameChat + "] " + element.toString());
        } else if (element instanceof ConnectDisconnectElement) {
            logger.trace("Remove last element connect/disconnect");
        }
        return element;
    }

    public void joinClient(Client client) { // Клиент вошел в этот чат
        logger.info("Client: " + client.getNickname() + " joined to chat under the name of " + this.toString());

        if (client.getRightNowChat() != null) { // Если клиент уже находится в каком то чате
            client.getRightNowChat().leaveClient(client); // Выходим из чата
        }

        client.setRightNowChat(this); // Устанавливаем клиенту что он сейчас находится в этом чате
        this.chatClients.add(client); // Добавляем его в пользователей этого чата


        try {
            Response responseNameChat = new Response(-1,0,this.nameChat,Response.Type.selfJoinToChat);
            client.getWebSocket().send(responseNameChat.toJSON());
            logger.debug("Send client response set name chat");
            if (!elementMessageInChatLinkedList.isEmpty()) { // Если есть сообщение в чате тогда отправляем список клиенту
                Response response = new Response(-1,0,objectMapper.writeValueAsString(elementMessageInChatLinkedList),Response.Type.listElementChat);
                client.getWebSocket().send(response.toJSON());
                logger.debug("Send client response with list message");
                this.removeLastElementInChat();
            }

        } catch (JsonProcessingException e) {
            logger.error(e.getMessage() + " Cause -> " + e.getCause());
        }

        this.notifyJoinClient(client); // Оповещаем пользователей чата о подключение нового клиента


    }

    public void leaveClient(Client client) { // Клиент вышел из чата
        logger.info("Client: " + client.getNickname() + " leave from chat under the name of " + this.toString());
        client.setRightNowChat(null); // Устанавливаем что клиент вышел из чата
        this.chatClients.remove(client); // Удаляем из списка чата
        this.notifyLeaveClient(client); // Оповещаем о выходе клиента
    }

    private void notifyJoinClient(Client client) {
        ElementMessageInChat element = new ConnectDisconnectElement(this.idElement,client.getNickname(),true);
        this.idElement++;
        this.addElementInChat(element,Response.Type.joinToChat);
    }

    private void notifyLeaveClient(Client client) {
        ElementMessageInChat element = new ConnectDisconnectElement(idElement,client.getNickname(),false); // Создаем элемент чата
        this.idElement++; // Увеличиваем айди
        this.addElementInChat(element,Response.Type.leaveFromChat); // Добавляем в чат элемент
    }

    private void sendAllClientInChat(Response response) {
        String jsonResponse = null;
        try {
            jsonResponse = response.toJSON(); // response парсим в JSON
        } catch (JsonProcessingException e) {
            logger.error(e.getMessage() + " Cause -> " + e.getCause());
        }

        assert jsonResponse != null;
        logger.debug("Notify all clients in " + this.nameChat + " JSON Message for Clients -> " + jsonResponse);
        for (Client c : this.chatClients) { // Отправлеям всем участинам чата
            try {
                c.getWebSocket().send(jsonResponse);
            } catch (WebsocketNotConnectedException e) {
                logger.debug("Web socket no longer exits! So remove client from clients list");
                this.chatClients.remove(c);
            }

        }
    }

    public synchronized LinkedList<ElementMessageInChat> getElementMessageInChatLinkedList() {
        return elementMessageInChatLinkedList;
    }

    public synchronized void setElementMessageInChatLinkedList(LinkedList<ElementMessageInChat> elementMessageInChatLinkedList) {
        this.elementMessageInChatLinkedList = elementMessageInChatLinkedList;
    }

    public synchronized int getMaxMessages() {
        return maxMessages;
    }

    public synchronized void setMaxMessages(int maxMessages) {
        this.maxMessages = maxMessages;
    }

    public synchronized String getNameChat() {
        return nameChat;
    }

    public synchronized void setNameChat(String nameChat) {
        this.nameChat = nameChat;
    }

    public List<Client> getChatClients() {
        return chatClients;
    }

    public String toString() {
        return "[" + this.nameChat + "]";
    }
}