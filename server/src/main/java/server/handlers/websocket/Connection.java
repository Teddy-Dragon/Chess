package server.handlers.websocket;

import chess.ChessGame;
import com.google.gson.Gson;
import org.eclipse.jetty.websocket.api.Session;
import websocket.messages.ServerMessage;

import java.io.IOException;

public class Connection {
    public String username;
    public Session session;
    public Connection(String username, Session session){
        this.username = username;
        this.session = session;
    }
    public void send(String message) throws Exception{
        ServerMessage serverMessage = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION);
        serverMessage.setMessage(message);
        session.getRemote().sendString(new Gson().toJson(serverMessage));
    }
    public void update(ChessGame game) throws IOException {
        ServerMessage serverMessage = new ServerMessage(ServerMessage.ServerMessageType.LOAD_GAME);
        serverMessage.setGame(game);
        session.getRemote().sendString(new Gson().toJson(serverMessage));
    }


}
