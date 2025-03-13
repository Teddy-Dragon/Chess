package dataaccess;

import model.GameData;

import java.util.HashMap;
import java.util.List;

public class SQLGameDAO implements GameDAO{
    public void clearAllGames() {

    }

    public void addGame(int gameID, GameData gameData) {

    }

    public GameData getGameByID(int gameID) {
        return null;
    }

    public HashMap<String, List<GameData>> getAllGames() {
        return null;
    }

    public void updateGame(int gameID, GameData newGameData) {

    }
    private final String[] createStatements = {
            """
                CREATE TABLE IF NOT EXISTS game(
                 'gameID' int NOT NULL,
                 'whiteUsername' varchar(256) DEFAULT NULL,
                 'blackUsername' varchar(256) DEFAULT NULL,
                 'gameName' varchar(256),
                 'game' ,
                 PRIMARY KEY ('gameID'),
                 INDEX(whiteUsername),
                 INDEX(blackUsername),
                 INDEX(gameName),
                 INDEX(game)            
                )"""
    };
}
