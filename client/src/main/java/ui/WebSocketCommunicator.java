package ui;

import exceptions.BadResponseException;
import json.JsonSerializer;
import websocket.commands.UserGameCommand;
import websocket.messages.ErrorMessage;
import websocket.messages.LoadGameMessage;
import websocket.messages.NotificationMessage;
import websocket.messages.ServerMessage;

import jakarta.websocket.*;

import java.net.URI;

public class WebSocketCommunicator extends Endpoint {

    private final Session session;
    private final ServerMessageManager messageManager;

    public WebSocketCommunicator(String url) {
        this.messageManager = new ServerMessageManager();
        try {
            url = url.replace("http", "ws");
            URI socketURI = new URI(url + "/ws");

            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            this.session = container.connectToServer(this, socketURI);

            addMessageHandler();
        } catch (Exception ex) {
            throw new BadResponseException(ex.getMessage(), 500);
        }
    }

    private void addMessageHandler() {
        this.session.addMessageHandler((MessageHandler.Whole<String>) this::handleMessage);
    }

    public void handleMessage(String jsonMessage) {
        ServerMessage serverMessage = JsonSerializer.fromJson(jsonMessage, ServerMessage.class);
        switch (serverMessage.getServerMessageType()) {
            case LOAD_GAME -> messageManager.loadGame(JsonSerializer.fromJson(jsonMessage, LoadGameMessage.class));
            case NOTIFICATION -> messageManager.notify(JsonSerializer.fromJson(jsonMessage, NotificationMessage.class));
            case ERROR -> messageManager.displayError(JsonSerializer.fromJson(jsonMessage, ErrorMessage.class));
        }
    }

    public void sendCommand(UserGameCommand command) {
        try {
            String jsonCommand = JsonSerializer.toJson(command);
            this.session.getBasicRemote().sendText(jsonCommand);
        } catch (Exception ex) {
            throw new BadResponseException(ex.getMessage(), 500);
        }
    }

    @Override
    public void onOpen(Session session, EndpointConfig endpointConfig) {
    }
}
