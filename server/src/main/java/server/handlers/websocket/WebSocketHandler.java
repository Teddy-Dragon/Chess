package server.handlers.websocket;

import chess.ChessBoard;
import chess.ChessGame;
import com.google.gson.Gson;
import dataaccess.AuthDAO;
import dataaccess.GameDAO;
import model.AuthData;
import model.GameData;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import websocket.commands.MakeMoveCommand;
import websocket.commands.UserGameCommand;
import websocket.messages.ServerMessage;

import java.io.IOException;
import java.util.Objects;
import java.util.UUID;

@WebSocket
public class WebSocketHandler {
    private final ConnectionManager connectionManager = new ConnectionManager();
    private AuthDAO authMap;
    private GameDAO gameMap;

    public WebSocketHandler(AuthDAO authMap, GameDAO gameMap){
        this.authMap = authMap;
        this.gameMap = gameMap;
    }

    @OnWebSocketMessage
    public void onMessage(Session session, String message){

        try{
            UserGameCommand userGameCommand = new Gson().fromJson(message, UserGameCommand.class);
            switch(userGameCommand.getCommandType()){
                case CONNECT -> connectHandler(session, userGameCommand.getAuthToken(), userGameCommand.getGameID());
                case LEAVE -> leaveHandler(userGameCommand);
                case RESIGN -> resignHandler(userGameCommand, session);
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
            AuthData playerInfo = verifyAuth(authToken, session);
             // throws an error when it can't turn it into a UUID String
            GameData gameData = gameMap.getGameByID(gameID);
            if(gameMap.getGameByID(gameID) == null){
                ServerMessage serverMessage = new ServerMessage(ServerMessage.ServerMessageType.ERROR);
                serverMessage.setErrorMessage("Error: Game does not exist");
                session.getRemote().sendString(new Gson().toJson(serverMessage));
                return;
            }
            else{
                connectionManager.add(playerInfo.username(), session, gameData.gameID(), gameData);
                ServerMessage serverMessage = new ServerMessage(ServerMessage.ServerMessageType.LOAD_GAME);
                serverMessage.setGame(gameData.game());
                session.getRemote().sendString(new Gson().toJson(serverMessage));
            }
        }catch (Exception e){
            System.out.println(e.getMessage());
        }

    }
    public void makeMoveHandler(MakeMoveCommand moveCommand, Session session){
        try{
            AuthData playerInfo = verifyAuth(moveCommand.getAuthToken(), session);
            GameData gameInfo = gameMap.getGameByID(moveCommand.getGameID());
            ChessGame game = gameInfo.game();
            ChessBoard board = game.getBoard();
            ChessGame.TeamColor pieceColor = game.getBoard().getPiece(moveCommand.getMove().getStartPosition()).getTeamColor();
            if(gameInfo.game().getIsGameOver()){
                ServerMessage serverMessage = new ServerMessage(ServerMessage.ServerMessageType.ERROR);
                serverMessage.setErrorMessage("Game is already over");
                session.getRemote().sendString(new Gson().toJson(serverMessage));
                return;
            }
            if(pieceColor == ChessGame.TeamColor.WHITE){
                if(game.getTeamTurn() != ChessGame.TeamColor.WHITE){
                    ServerMessage serverMessage = new ServerMessage(ServerMessage.ServerMessageType.ERROR);
                    serverMessage.setErrorMessage("It is not your turn!");
                    session.getRemote().sendString(new Gson().toJson(serverMessage));
                    return;
                }
                if(Objects.equals(playerInfo.username(), gameInfo.whiteUsername())){
                    commitMove(game, moveCommand, gameInfo, playerInfo, session);
                    checkGameStatus(moveCommand);
                }else{
                    ServerMessage serverMessage = new ServerMessage(ServerMessage.ServerMessageType.ERROR);
                    serverMessage.setErrorMessage("It is not your turn!");
                    session.getRemote().sendString(new Gson().toJson(serverMessage));
                }
            }
            else if(pieceColor == ChessGame.TeamColor.BLACK){
                if(game.getTeamTurn() != ChessGame.TeamColor.BLACK){
                    ServerMessage serverMessage = new ServerMessage(ServerMessage.ServerMessageType.ERROR);
                    serverMessage.setErrorMessage("It is not your turn!");
                    session.getRemote().sendString(new Gson().toJson(serverMessage));
                    return;
                }
                if(Objects.equals(playerInfo.username(), gameInfo.blackUsername())){
                    commitMove(game, moveCommand, gameInfo, playerInfo, session);
                    checkGameStatus(moveCommand);
                }
                else{
                    ServerMessage serverMessage = new ServerMessage(ServerMessage.ServerMessageType.ERROR);
                    serverMessage.setErrorMessage("It is not your turn!");
                    session.getRemote().sendString(new Gson().toJson(serverMessage));}
            }else{
                ServerMessage serverMessage = new ServerMessage(ServerMessage.ServerMessageType.ERROR);
                serverMessage.setErrorMessage("Error: move not valid");
                session.getRemote().sendString(new Gson().toJson(serverMessage));
            }


        }catch(Exception e){
            System.out.println(e.getMessage());
        }

    }
    public void leaveHandler(UserGameCommand userGameCommand) throws Exception {
        AuthData userInfo = authMap.getAuth(UUID.fromString(userGameCommand.getAuthToken()));
        GameData gameData = gameMap.getGameByID(userGameCommand.getGameID());
        GameData updatedData;
        if(Objects.equals(userInfo.username(), gameData.blackUsername())){
            updatedData = new GameData(gameData.gameID(), gameData.whiteUsername(), null, gameData.gameName(), gameData.game());
            gameMap.updateGame(gameData.gameID(), updatedData);
        }
        if(Objects.equals(userInfo.username(), gameData.whiteUsername())) {
            updatedData = new GameData(gameData.gameID(), null, gameData.blackUsername(), gameData.gameName(), gameData.game());
            gameMap.updateGame(gameData.gameID(), updatedData);
        }
        connectionManager.remove(userInfo.username(), userGameCommand.getGameID());
    }
    public void resignHandler(UserGameCommand userGameCommand, Session session) throws Exception {
        GameData gameData = gameMap.getGameByID(userGameCommand.getGameID());
        AuthData resigningPlayer = authMap.getAuth(UUID.fromString(userGameCommand.getAuthToken()));

        ChessGame game = gameData.game();
        if(!Objects.equals(resigningPlayer.username(), gameData.whiteUsername()) && !Objects.equals(resigningPlayer.username(), gameData.blackUsername())){
            ServerMessage serverMessage = new ServerMessage(ServerMessage.ServerMessageType.ERROR);
            serverMessage.setErrorMessage("Error: Observers cannot resign");
            session.getRemote().sendString(new Gson().toJson(serverMessage));
            return;
        }
        game.setIsGameOver(true);
        GameData updatedGame = new GameData(gameData.gameID(), gameData.whiteUsername(), gameData.blackUsername(), gameData.gameName(), game);
        gameMap.updateGame(gameData.gameID(), updatedGame);

        connectionManager.broadcastLobby("", resigningPlayer.username() + " has resigned, game is over!", gameData.gameID());
        connectionManager.remove(resigningPlayer.username(), userGameCommand.getGameID());




    }
    public void checkGameStatus(MakeMoveCommand moveCommand) throws Exception {
        GameData gameData = gameMap.getGameByID(moveCommand.getGameID());
        String isInCheck = "";
        String isCheckmate = "";
        String checkMessage = isInCheck + " is in Check!";
        String mateMessage = isCheckmate + " is in Checkmate, good game!";
        if(gameData.game().isInCheck(ChessGame.TeamColor.WHITE)){
            isInCheck = gameData.whiteUsername();
        }
        else if(gameData.game().isInCheck(ChessGame.TeamColor.BLACK)){
            isInCheck = gameData.blackUsername();
        }
        if(gameData.game().isInCheckmate(ChessGame.TeamColor.WHITE)){
            isCheckmate = gameData.whiteUsername();
        } else if (gameData.game().isInCheckmate(ChessGame.TeamColor.BLACK)) {
            isCheckmate = gameData.blackUsername();
        }

        if(!isInCheck.isEmpty() && isCheckmate.isEmpty()){
            connectionManager.broadcastLobby("", isInCheck + checkMessage, gameData.gameID() );
        }
        if(!isCheckmate.isEmpty()){
            ChessGame game = gameData.game();
            game.setIsGameOver(true);
            GameData updatedGame = new GameData(gameData.gameID(), gameData.whiteUsername(), gameData.blackUsername(), gameData.gameName(), game);
            gameMap.updateGame(gameData.gameID(), updatedGame);
            connectionManager.broadcastLobby("", isCheckmate + mateMessage, gameData.gameID());
        }

    }
    private void commitMove(ChessGame game, MakeMoveCommand moveCommand, GameData gameInfo, AuthData playerInfo, Session session) throws Exception {;
        if(game.isAValidMove(moveCommand.getMove())){
            game.makeMove(moveCommand.getMove());
            gameMap.updateGame(moveCommand.getGameID(), new GameData(moveCommand.getGameID(),
                    gameInfo.whiteUsername(), gameInfo.blackUsername(), gameInfo.gameName() ,game));
            ServerMessage serverMessage = new ServerMessage(ServerMessage.ServerMessageType.LOAD_GAME);
            serverMessage.setGame(gameMap.getGameByID(gameInfo.gameID()).game());
            session.getRemote().sendString(new Gson().toJson(serverMessage));
            connectionManager.gameUpdate(playerInfo.username(), gameInfo.gameID(), game, moveCommand.getMove());
        }else{
            ServerMessage serverMessage = new ServerMessage(ServerMessage.ServerMessageType.ERROR);
            serverMessage.setErrorMessage("Error: Invalid Move!");
            session.getRemote().sendString(new Gson().toJson(serverMessage));
        }
    }

    private AuthData verifyAuth(String authToken, Session session) throws IOException {
        try{
            AuthData playerInfo;
            playerInfo = authMap.getAuth(UUID.fromString(authToken));
            return playerInfo;
        }
        catch(Exception e){
            ServerMessage serverMessage = new ServerMessage(ServerMessage.ServerMessageType.ERROR);
            serverMessage.setErrorMessage("Error: Not Authorized");
            session.getRemote().sendString(new Gson().toJson(serverMessage));
        }
        return null;
    }


}