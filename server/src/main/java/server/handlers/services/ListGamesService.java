package server.handlers.services;

import dataaccess.MemoryGameDAO;
import model.ListGame;

public class ListGamesService {
    //if GameHandler is faced with a get method it calls this
    //AuthDAO.getAuth to make sure you have permission to do anything
    //GameDAO.getAllGames to return all games
    private final MemoryGameDAO gameMap;


    public ListGamesService(MemoryGameDAO gameMap) {
        this.gameMap = gameMap;
    }
    public ListGame listGames(){
        return gameMap.getAllGames();

    }
}
