package server.handlers.services;

import dataaccess.*;
import model.GameData;

import java.util.Objects;

public class JoinGameService {
    // if GameHandler is met with a put request it comes here
    //GameDAO.findGameByID with game ID from request
    //Modify returned game data with player info from player color
    //GameDAO.updateGame with updated model to add player to game
    private final UserDAO userMap;
    private final GameDAO gameMap;
    private final AuthDAO authMap;

    public JoinGameService(UserDAO userMap, GameDAO gameMap, AuthDAO authMap) {
        this.userMap = userMap;
        this.gameMap = gameMap;
        this.authMap = authMap;
    }

    public Object joinGame(String playerColor, int gameID, String username) throws Exception{


        GameData game = gameMap.getGameByID(gameID);
        if(game == null){
            throw new Exception("Error: bad request");
        }
        if(!Objects.equals(playerColor, "WHITE") && !Objects.equals(playerColor, "BLACK")){
            throw new Exception("Error: bad request");
        }
        if(game.blackUsername() != null && Objects.equals(playerColor, "BLACK")){
            throw new Exception("Error: already taken");

        }
        if(game.whiteUsername() != null && Objects.equals(playerColor, "WHITE")){
            throw new Exception("Error: already taken");
        }
        if(Objects.equals(playerColor, "WHITE")) {
            GameData newData = new GameData(gameID, username, game.blackUsername(), game.gameName(), game.game());
            gameMap.updateGame(gameID, newData);
        }
        if(Objects.equals(playerColor, "BLACK")){
            GameData newData = new GameData(gameID, game.whiteUsername(), username, game.gameName(), game.game());
            gameMap.updateGame(gameID, newData);
        }

        return null;


    }
}
