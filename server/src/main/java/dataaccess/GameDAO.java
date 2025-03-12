package dataaccess;

import model.GameData;

public interface GameDAO {
    public void clearAllGames();
    public void addGame(GameData gameData);
    public GameData getGameByID(int gameID);
    public GameData getAllGames();
    public void updateGame(int gameID);
}
