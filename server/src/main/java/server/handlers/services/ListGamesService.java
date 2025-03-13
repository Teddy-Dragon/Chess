package server.handlers.services;

import dataaccess.GameDAO;
import model.GameData;

import java.util.HashMap;
import java.util.List;

public class ListGamesService {
    //if GameHandler is faced with a get method it calls this
    //AuthDAO.getAuth to make sure you have permission to do anything
    //GameDAO.getAllGames to return all games
    private final GameDAO gameMap;


    public ListGamesService(GameDAO gameMap) {
        this.gameMap = gameMap;
    }
    public HashMap<String, List<GameData>> listGames(){
        return gameMap.getAllGames();

    }
}
