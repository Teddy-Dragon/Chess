package server.handlers.services;

import DataAccess.MemoryGameDAO;
import model.GameData;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ListGamesService {
    //if GameHandler is faced with a get method it calls this
    //AuthDAO.getAuth to make sure you have permission to do anything
    //GameDAO.getAllGames to return all games
    private final MemoryGameDAO gameMap;


    public ListGamesService(MemoryGameDAO gameMap) {
        this.gameMap = gameMap;
    }
    public HashMap<String, List<GameData>> ListGames(){
        return gameMap.getAllGames();

    }
}
