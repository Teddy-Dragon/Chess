package server.handlers.websocket;

import chess.ChessBoard;
import chess.ChessGame;
import com.google.gson.Gson;
import dataaccess.AuthDAO;
import dataaccess.GameDAO;
import dataaccess.SQLAuthDAO;
import dataaccess.SQLGameDAO;
import model.AuthData;
import model.GameData;
import model.Notification;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import websocket.commands.MakeMoveCommand;
import websocket.commands.UserGameCommand;
import websocket.messages.ServerMessage;

import java.util.Objects;
import java.util.UUID;

@WebSocket
public class WebSocketHandler {
    private final ConnectionManager connectionManager = new ConnectionManager();
    private AuthDAO authMap;
    private GameDAO gameMap;

    @OnWebSocketMessage
    public void onMessage(Session session, String message){

        try{
            authMap = new SQLAuthDAO();
            gameMap = new SQLGameDAO();
            UserGameCommand userGameCommand = new Gson().fromJson(message, UserGameCommand.class);
            switch(userGameCommand.getCommandType()){
                case CONNECT -> connectHandler(session, userGameCommand.getAuthToken(), userGameCommand.getGameID());
                case LEAVE -> leaveHandler();
                case RESIGN -> resignHandler();
                case MAKE_MOVE -> {
                    MakeMoveCommand moveCommand = new Gson().fromJson(message, MakeMoveCommand.class);
                    makeMoveHandler(moveCommand, session);
                }
            }
        }catch(Exception e){
            System.out.println(e.getMessage());
        }

    }

    public void connectHandler(Session session, String authToken, Integer gameID){
        try{
            AuthData playerInfo = authMap.getAuth(UUID.fromString(authToken));
            if(gameMap.getGameByID(gameID) == null || playerInfo == null){
                session.getRemote().sendString(new Gson().toJson(new ServerMessage(ServerMessage.ServerMessageType.ERROR)));
            }
            connectionManager.add(playerInfo.username(), session);
            session.getRemote().sendString(new Gson().toJson(new ServerMessage(ServerMessage.ServerMessageType.LOAD_GAME)));
        }catch (Exception e){
            System.out.println(e.getMessage());
        }

    }
    public void makeMoveHandler(MakeMoveCommand moveCommand, Session session){
        try{
            AuthData playerInfo = authMap.getAuth(UUID.fromString(moveCommand.getAuthToken()));
            GameData gameInfo = gameMap.getGameByID(moveCommand.getGameID());
            ChessGame game = gameInfo.game();
            ChessBoard board = game.getBoard();
            ChessGame.TeamColor pieceColor = game.getBoard().getPiece(moveCommand.getMove().getStartPosition()).getTeamColor();
            if(pieceColor == ChessGame.TeamColor.WHITE){
                if(Objects.equals(playerInfo.username(), gameInfo.whiteUsername())){
                    if(game.isAValidMove(moveCommand.getMove())){
                        board.commitMove(moveCommand.getMove(), board.getPiece(moveCommand.getMove().getStartPosition()));
                        gameMap.updateGame(moveCommand.getGameID(), new GameData(moveCommand.getGameID(),
                                gameInfo.whiteUsername(), gameInfo.blackUsername(), gameInfo.gameName() ,game));
                    }
                }
            }
            if(pieceColor == ChessGame.TeamColor.BLACK){
                if(Objects.equals(playerInfo.username(), gameInfo.blackUsername())){
                    if(game.isAValidMove(moveCommand.getMove())){
                        board.commitMove(moveCommand.getMove(), board.getPiece(moveCommand.getMove().getStartPosition()));
                        gameMap.updateGame(moveCommand.getGameID(), new GameData(moveCommand.getGameID(),
                                gameInfo.whiteUsername(), gameInfo.blackUsername(), gameInfo.gameName() ,game));
                    }
                }
            }else{
                Notification notification = new Notification(new ServerMessage(ServerMessage.ServerMessageType.LOAD_GAME), gameMap.getGameByID(gameInfo.gameID()));
                session.getRemote().sendString(new Gson().toJson(notification));
            }


        }catch(Exception e){
            System.out.println(e.getMessage());
        }

    }
    public void leaveHandler(){}
    public void resignHandler(){}



}