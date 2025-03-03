package server.handlers.services;

import DataAccess.MemoryAuthDAO;
import DataAccess.MemoryGameDAO;
import DataAccess.MemoryUserDAO;
import model.IncorrectResponse;
import model.GameData;

import java.util.Objects;

public class JoinGameService {
    // if GameHandler is met with a put request it comes here
    //GameDAO.findGameByID with game ID from request
    //Modify returned game data with player info from player color
    //GameDAO.updateGame with updated model to add player to game
    private final MemoryUserDAO userMap;
    private final MemoryGameDAO gameMap;
    private final MemoryAuthDAO authMap;

    public JoinGameService(MemoryUserDAO userMap, MemoryGameDAO gameMap, MemoryAuthDAO authMap) {
        this.userMap = userMap;
        this.gameMap = gameMap;
        this.authMap = authMap;
    }

    public IncorrectResponse joinGame(String playerColor, int gameID, String username){
        GameData game = gameMap.getGameByID(gameID);
        if(game.blackUsername() != null && Objects.equals(playerColor, "BLACK")){
            IncorrectResponse newError = new IncorrectResponse(false, false, true, null);
            return newError;
        }
        if(game.whiteUsername() != null && Objects.equals(playerColor, "WHITE")){
            IncorrectResponse newError = new IncorrectResponse(false, false, true, null);
            return newError;
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
