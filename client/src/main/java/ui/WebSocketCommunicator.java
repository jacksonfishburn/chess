package ui;

import exceptions.BadResponseException;
import json.JsonSerializer;
import websocket.messages.ServerMessage;

import jakarta.websocket.*;

import java.net.URI;

public class WebSocketCommunicator extends Endpoint {

    Session session;

    public WebSocketCommunicator(String url) {
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
        this.session.addMessageHandler(new MessageHandler.Whole<String>() {
            @Override
            public void onMessage(String message) {
                ServerMessage serverMessage = JsonSerializer.fromJson(message, ServerMessage.class);
            }
        });
    }

    @Override
    public void onOpen(Session session, EndpointConfig endpointConfig) {
    }
}
