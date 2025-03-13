package server.handlers.services;

import chess.ChessGame;
import dataaccess.GameDAO;
import model.GameData;

import java.util.Random;

public class CreateGameService {
    // if GameHandler is met with a post request, it goes here
    //Generate a gameID and create a GameData model to send to GameDAO.addGame(Created model)
    private final GameDAO gameMap;


    public CreateGameService(GameDAO gameMap) {
        this.gameMap = gameMap;
    }

    public GameData makeGame(String gameName) throws Exception{
        Random random = new Random();
        int id = random.nextInt(100000, 999999);
        while(gameMap.getGameByID(id) != null){
            id = random.nextInt(100000, 999999);
        }
        GameData game = new GameData(id, null, null, gameName, new ChessGame());
        gameMap.addGame(game);
        return game;

        //auth status should be checked in GameHandler
    }
}
