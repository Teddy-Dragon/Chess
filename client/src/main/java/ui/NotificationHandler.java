package ui;

import websocket.messages.ServerMessage;

public interface NotificationHandler {
    public void notify(ServerMessage serverMessage);
}
