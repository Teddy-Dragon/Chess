package data;

import model.GameData;

import java.util.*;

public class MemoryGameDAO{
    private final HashMap<Integer, GameData> gameMap;

    public MemoryGameDAO(HashMap<Integer, GameData> gameMap) {
        this.gameMap = gameMap;
    }

    public void clearAllGames(){
        gameMap.clear();
        //remove all Game data from database
    }
    public void addGame(int gameID, GameData gameData){
        gameMap.put(gameID, gameData);
        //makes and stores a new game
    }
    public GameData getGameByID(int gameID){
        return gameMap.get(gameID);
        //Returns game data from games with supplied gameData
    }
    public HashMap<String, List<GameData>> getAllGames(){
        //void temporarily, returns all active games regardless of ID
        HashMap<String, List<GameData>> allGames = new HashMap<String, List<GameData>>();
        List<GameData> gameData = new ArrayList<>();
        for(Map.Entry<Integer, GameData> i : gameMap.entrySet()){
            gameData.add(i.getValue());
        }
        allGames.put("games", gameData);
        return allGames;
    }
//    public void deleteGame(int gameID){
//        GameData data = gameMap.get(gameID);
//        gameMap.remove(gameID, data);
//        //deletes one individual game from the database
//    }
    public void updateGame(int gameID, GameData newGameData){
        // updates a currently active game based off of game ID
        GameData oldData = gameMap.get(gameID);
        gameMap.remove(gameID, oldData);
        gameMap.put(gameID, newGameData);
    }
}
