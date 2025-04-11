package model;

import websocket.messages.ServerMessage;

public record Notification(
        ServerMessage serverMessage,
        Object returnData
) {
}
