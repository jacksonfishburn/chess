package websocket.messages;

public class NotificationMessage extends ServerMessage {

    private final String message;

    public NotificationMessage(String message) {
        type = ServerMessageType.LOAD_GAME;
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
