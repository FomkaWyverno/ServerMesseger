package com.wyverno.server.model.client.chat;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wyverno.server.model.Response;
import com.wyverno.server.model.client.Client;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public abstract class Chat {
    protected static final Logger logger = LoggerFactory.getLogger(Chat.class);
    private static final ObjectMapper objectMapper = new ObjectMapper();

    private static final int DEFAULT_MAX_MESSAGES = 60;

    private LinkedList<ElementMessageInChat> elementMessageInChatLinkedList = new LinkedList<>(); // Элементы чаты
    private LinkedList<Message> messages = new LinkedList<>();
    private List<Client> chatClients; // Клиенты в чате
    private int idElement = 0; // Самый последний айди элемента
    private int maxMessages; // Максимальное количество сообщений
    private String nameChat; // Название чата

    public Chat(String nameChat, int maxMessages, List<Client> chatClients) {
        this.nameChat = nameChat;
        this.maxMessages = maxMessages;
        this.chatClients = chatClients;
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
        this.checkAndRemoveAfterLimitElement();
        try {
            Response response = new Response(-1,0,message.toJSON(),Response.Type.message);
            this.sendAllClientInChat(response);
            this.elementMessageInChatLinkedList.addFirst(message); // Добавляем в начало списка сообщение
            this.messages.addFirst(message);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }


    }

    public void checkAndRemoveAfterLimitElement() {
        while (this.elementMessageInChatLinkedList.size() >= this.maxMessages) { // Проверяем чат на максимальное количество сообщений
            ElementMessageInChat element = elementMessageInChatLinkedList.removeLast(); // Удаляем последний элемент из списка

            if (element instanceof Message) {
                logger.trace("Remove last message in chat [" + this.nameChat + "] " + element.toString()); // Логируем что мы его удалили
                this.messages.remove(element);
            } else if (element instanceof ConnectDisconnectElement) {
                logger.trace("Remove last element connect/disconnect ");
            }

            try {
                Response response = new Response(-1,0,element.toJSON(),Response.Type.deleteElement);
                this.sendAllClientInChat(response);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }
    }

    public void joinClient(Client client) { // Клиент вошел в этот чат
        logger.info("Client: " + client.getNickname() + " joined to chat under the name of " + this.toString());
        client.setRightNowChat(this); // Устанавливаем клиенту что он сейчас находится в этом чате
        this.chatClients.add(client); // Добавляем его в пользователей этого чата


        try {
            Response responseNameChat = new Response(-1,0,this.nameChat,Response.Type.setNameChat);
            client.getWebSocket().send(responseNameChat.toJSON());
            logger.debug("Send client response set name chat");
            if (!elementMessageInChatLinkedList.isEmpty()) {
                Response response = new Response(-1,0,objectMapper.writeValueAsString(messages),Response.Type.listMessages);
                client.getWebSocket().send(response.toJSON());
                logger.debug("Send client response with list message");
                this.checkAndRemoveAfterLimitElement();
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

        Response response = null;
        try {
            response = new Response(-1,0, element.toJSON(), Response.Type.joinToChat);
            logger.debug("Created response for element");
        } catch (JsonProcessingException e) {
            logger.error(e.getMessage() + " Cause -> " + e.getCause());
        }
        assert response != null;

        this.elementMessageInChatLinkedList.addFirst(element);
        sendAllClientInChat(response); // Создали отклик и отправляем всем участинкам чата
    }

    private void notifyLeaveClient(Client client) {
        Response response = new Response(-1, 0, client.getNickname(),Response.Type.leaveFromChat);
        ElementMessageInChat element = new ConnectDisconnectElement(idElement,client.getNickname(),false);
        this.elementMessageInChatLinkedList.addFirst(element);
        this.idElement++;
        sendAllClientInChat(response); // Создали отклик и отправляем всем участникам чата
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
            c.getWebSocket().send(jsonResponse);
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

    public String toString() {
        return "[" + this.nameChat + "]";
    }
}