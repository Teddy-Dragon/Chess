package DataAccess;

import model.GameData;

import java.util.HashMap;

public class MemoryGameDAO implements GameDAO {
    private final HashMap<Integer, GameData> gameMap;

    public MemoryGameDAO(HashMap<Integer, GameData> gameMap) {
        this.gameMap = gameMap;
    }

    public void clearAllGames(){
        gameMap.clear();
        //remove all Game data from database
    }
    public void addGame(GameData gameData){
        //makes and stores a new game
    }
    public GameData getGameByID(int gameID){
        //void temporarily, returns game data from games with supplied gameData
        return null;
    }
    public GameData getAllGames(){
        //void temporarily, returns all active games regardless of ID
        return null;
    }
    public void deleteGame(int gameID){
        //deletes one individual game from the database
    }
    public void updateGame(int gameID){
        // updates a currently active game based off of game ID
    }
}
