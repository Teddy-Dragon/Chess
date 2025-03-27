package dataaccess;

import model.GameData;
import model.ListGame;

import java.util.*;

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
    public ListGame getAllGames(){
        //void temporarily, returns all active games regardless of ID
        ListGame allGames = new ListGame(new HashMap<String, List<GameData>>());
        List<GameData> gameData = new ArrayList<>();
        for(Map.Entry<Integer, GameData> i : gameMap.entrySet()){
            gameData.add(i.getValue());
        }
        allGames.gameList().put("games", gameData);
        return allGames;
    }

    public void updateGame(int gameID, GameData newGameData){
        // updates a currently active game based off of game ID
        GameData oldData = gameMap.get(gameID);
        gameMap.remove(gameID, oldData);
        gameMap.put(gameID, newGameData);
    }
}
