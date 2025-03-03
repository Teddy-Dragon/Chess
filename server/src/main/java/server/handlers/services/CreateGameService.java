package server.handlers.services;

import DataAccess.MemoryAuthDAO;
import DataAccess.MemoryGameDAO;
import DataAccess.MemoryUserDAO;
import chess.ChessGame;
import model.GameData;

import java.util.Random;

public class CreateGameService {
    // if GameHandler is met with a post request, it goes here
    //Generate a gameID and create a GameData model to send to GameDAO.addGame(Created model)
    private final MemoryUserDAO userMap;
    private final MemoryGameDAO gameMap;
    private final MemoryAuthDAO authMap;


    public CreateGameService(MemoryUserDAO userMap, MemoryGameDAO gameMap, MemoryAuthDAO authMap) {
        this.userMap = userMap;
        this.gameMap = gameMap;
        this.authMap = authMap;
    }

    public GameData makeGame(String gameName){
        Random random = new Random();
        int id = random.nextInt(100000, 999999);
        while(gameMap.getGameByID(id) != null){
            id = random.nextInt(100000, 999999);
        }
        GameData game = new GameData(id, null, null, gameName, new ChessGame());
        gameMap.addGame(id, game);
        return game;

        //auth status should be checked in GameHandler
    }
}
