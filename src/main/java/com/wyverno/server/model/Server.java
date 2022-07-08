package com.wyverno.server.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wyverno.server.model.client.Session;
import com.wyverno.server.model.client.chat.Chat;
import com.wyverno.server.model.client.chat.PrivateChat;
import com.wyverno.server.model.events.AbstractEvent;
import com.wyverno.server.model.events.Event;
import com.wyverno.server.model.events.Events;
import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;
import org.slf4j.LoggerFactory;

import org.slf4j.Logger;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Server extends WebSocketServer {

    private static final Logger logger = LoggerFactory.getLogger(Server.class);
    private static final ObjectMapper objectMapper = new ObjectMapper();

    private static final int DEFAULT_PORT = 4747;


    private final HashMap<String, Method> eventMethods = new HashMap<>();

    private final HashMap<WebSocket, Session> clientHashMap = new HashMap<>();
    private final List<PrivateChat> chatList;
    public final Chat GLOBAL_CHAT;

    public Server (InetSocketAddress address, List<PrivateChat> chatList, Chat globalChat) {
        super(address);
        this.chatList = chatList;
        this.GLOBAL_CHAT = globalChat;
        this.initEvents();
    }

    public Server(int port, List<PrivateChat> chatList, Chat globalChat) {
        super(new InetSocketAddress(port));
        this.chatList = chatList;
        this.GLOBAL_CHAT = globalChat;
        this.initEvents();
    }

    public Server(InetSocketAddress address, Chat globalChat) {
        super(address);
        this.chatList = new ArrayList<>();
        this.GLOBAL_CHAT = globalChat;
        this.initEvents();
    }

    public Server(int port, Chat globalChat) {
        super(new InetSocketAddress(port));
        this.chatList = new ArrayList<>();
        this.GLOBAL_CHAT = globalChat;
        this.initEvents();
    }

    public Server(Chat globalChat) {
        super(new InetSocketAddress(DEFAULT_PORT));
        this.chatList = new ArrayList<>();
        this.GLOBAL_CHAT = globalChat;
        this.initEvents();
    }



    @Override
    public void onOpen(WebSocket webSocket, ClientHandshake clientHandshake) {
        logger.info("Connected client: " + webSocket.getRemoteSocketAddress().getHostString() + ":" + webSocket.getRemoteSocketAddress().getPort());
        // Логируем информацию о подключенном пользователе
    }

    @Override
    public void onClose(WebSocket webSocket, int i, String s, boolean b) {
        logger.info("Disconnected client: " + webSocket.getRemoteSocketAddress().getHostString() + ":" + webSocket.getRemoteSocketAddress().getPort());
        // Логируем информацию о отключеном пользователе
        Session client = clientHashMap.get(webSocket);
        logger.info("Disconnected nick: " + client.getNickname());
        this.clientHashMap.remove(webSocket);
        Chat clientChat = client.getRightNowChat();
        clientChat.leaveClient(client);
    }

    @Override
    public void onMessage(WebSocket webSocket, String message) {
        logger.debug("Received a message from a client: " + message); // Логируем сообщение от пользователя
        logger.debug("Received from the user " + webSocket.getRemoteSocketAddress()); // Логируем от кого пришло сообщение
        try {
            JsonNode jsonNode = objectMapper.readTree(message); // Парсим в JsonNode сообжение
            logger.trace("Read json"); // Оповещаем что мы прочитали json
            processingRequest(jsonNode,webSocket); // Вызываем метод процесса обработки запроса
        } catch (JsonProcessingException e) {
            logger.error("The message is not JSON or the message is have bug with JSON file.\n" + e.getCause()); // Оповещаем о ошибке в логгер
        }
    }

    @Override
    public void onError(WebSocket webSocket, Exception e) {
        logger.error("Error client: " + webSocket.getRemoteSocketAddress().getHostString() + ":" +
                webSocket.getRemoteSocketAddress().getPort() + " ERROR -> " + e);
    }

    @Override
    public void onStart() {
        logger.info("Server started ip-address is " + this.getAddress().getHostName() + ":" + this.getPort());
        // Оповещаем логера о запуске сервера
    }

    private void initEvents() { // Инизилизируем методы для ивентов
        Method[] methods = Events.class.getDeclaredMethods();

        for (Method method : methods) { // Проходимся по каждому методу
            if (method.isAnnotationPresent(Event.class)) { // Проверяем если в метода анотация @Event

                Event event = method.getAnnotation(Event.class); // Берем эту анотацию
                this.eventMethods.put(event.type(), method); // Добавляем в карту как ключ тип, как значение метод
            }
        }
    }

    private void processingRequest(JsonNode jsonNode, WebSocket webSocket) {
        logger.trace("Processing request...");
        JsonNode dataNode = jsonNode.get("data");

        logger.debug("Check if contains key is type: " + dataNode.get("type").asText());
        if (this.eventMethods.containsKey(dataNode.get("type").asText())) { // Проверяем есть ли такой тип ивента
            try {
                Method method = this.eventMethods.get(dataNode.get("type").asText());
                AbstractEvent event = (AbstractEvent) method.invoke(Events.class,dataNode,webSocket,jsonNode.get("requestID").asInt(),this); // Создаем обьект ивента

                event.runEvent(); // Выполняем ивент
            } catch (IllegalAccessException | InvocationTargetException e) {
                logger.error("When executing method.invoke got an exception");
            }
        } else { // Если не существует ивента который прислал пользователь
            logger.warn("User send type event which is not");
        }
    }

    public HashMap<WebSocket, Session> getClientHashMap() {
        return clientHashMap;
    }

    public List<PrivateChat> getChatList() {
        return chatList;
    }

    public void addChat(PrivateChat chat) {
        logger.info("Add chat -> " + chat.toString());
        this.chatList.add(chat);
    }

    public void removeChat(PrivateChat chat) {
        logger.info("Remove chat -> " + chat.toString());
        this.chatList.remove(chat);
    }

    public void update(PrivateChat chat) { // Наблюдаемый обьект обновился
        logger.trace("List clients in chat -> " + chat.getChatClients());
        if (chat.getChatClients().isEmpty()) { // Если в чате нету пользователей то удаляем чат
            logger.info("Removed chat -> " + chat);
            this.chatList.remove(chat);
        }
    }
}
