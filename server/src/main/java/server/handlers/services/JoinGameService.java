package server.handlers.services;

import DataAccess.MemoryAuthDAO;
import DataAccess.MemoryGameDAO;
import DataAccess.MemoryUserDAO;
import model.GameData;

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

    public void joinGame(String playerColor, int gameID){
        GameData game = gameMap.getGameByID(gameID);


    }
}
