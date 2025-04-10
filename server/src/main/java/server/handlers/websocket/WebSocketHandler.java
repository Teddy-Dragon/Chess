package server.handlers.websocket;

import com.google.gson.Gson;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import websocket.commands.MakeMoveCommand;
import websocket.commands.UserGameCommand;

@WebSocket
public class WebSocketHandler {
    private final ConnectionManager connectionManager = new ConnectionManager();

    @OnWebSocketMessage
    public void onMessage(Session session, String message){
        try{
            UserGameCommand userGameCommand = new Gson().fromJson(message, UserGameCommand.class);
            switch(userGameCommand.getCommandType()){
                case CONNECT -> connectHandler(userGameCommand.getAuthToken(), userGameCommand.getGameID());
                case LEAVE -> leaveHandler();
                case RESIGN -> resignHandler();
                case MAKE_MOVE -> {
                    MakeMoveCommand moveCommand = new Gson().fromJson(message, MakeMoveCommand.class);
                    makeMoveHandler(moveCommand);
                }
            }
        }catch(Exception e){
            System.out.println(e.getMessage());
        }

    }

    public void connectHandler(String authToken, Integer gameID){

    }
    public void makeMoveHandler(MakeMoveCommand moveCommand){

    }
    public void leaveHandler(){}
    public void resignHandler(){}



}