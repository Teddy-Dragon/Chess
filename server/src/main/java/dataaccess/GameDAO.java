package dataaccess;

import model.GameData;

import java.util.List;

public interface GameDAO {
    public void clearAllGames();
    public void addGame(GameData gameData);
    public GameData getGameByID(int gameID);
    public List<GameData> getAllGames();
    public void updateGame(int gameID, GameData newGameData);
}
