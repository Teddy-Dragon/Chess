package dataaccess;

import model.GameData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MemoryGameDAO implements GameDAO{
    private final HashMap<Integer, GameData> gameMap;

    public MemoryGameDAO(HashMap<Integer, GameData> gameMap) {
        this.gameMap = gameMap;
    }

    public void clearAllGames(){
        gameMap.clear();
        //remove all Game data from database
    }
    public void addGame(GameData gameData){
        gameMap.put(gameData.gameID(), gameData);
        //makes and stores a new game
    }
    public GameData getGameByID(int gameID){
        return gameMap.get(gameID);
        //Returns game data from games with supplied gameData
    }
    public List<GameData> getAllGames(){
        //void temporarily, returns all active games regardless of ID
        List<GameData> gameData = new ArrayList<>();
        for(Map.Entry<Integer, GameData> i : gameMap.entrySet()){
            gameData.add(i.getValue());
        }
        return gameData;
    }

    public void updateGame(int gameID, GameData newGameData){
        // updates a currently active game based off of game ID
        GameData oldData = gameMap.get(gameID);
        gameMap.remove(gameID, oldData);
        gameMap.put(gameID, newGameData);
    }
}
