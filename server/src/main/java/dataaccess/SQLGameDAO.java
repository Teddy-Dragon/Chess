package dataaccess;

import model.GameData;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

public class SQLGameDAO implements GameDAO{
    public SQLGameDAO() throws Exception{
        configureDatabase();
    }
    public void clearAllGames() {
        String deleteStatements = "DROP TABLE IF EXISTS game";
        try(var conn = DatabaseManager.getConnection()){
            try(var preparedStatement = conn.prepareStatement(deleteStatements)){
                preparedStatement.executeUpdate();
            }
        }catch(Exception ignored){

        }

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
                 `gameID` int NOT NULL,
                 `whiteUsername` varchar(256) DEFAULT NULL,
                 `blackUsername` varchar(256) DEFAULT NULL,
                 `gameName` varchar(256),
                 `game` TEXT NOT NULL,
                 PRIMARY KEY (`gameID`),
                 INDEX(whiteUsername),
                 INDEX(blackUsername),
                 INDEX(gameName)
                )"""
    };

    private void configureDatabase() throws Exception {
        DatabaseManager.createDatabase();
        try(var conn = DatabaseManager.getConnection()){
            for(var statement : createStatements){
                try(var preparedStatement = conn.prepareStatement(statement)){
                    preparedStatement.executeUpdate();
                }
            }
        }catch(SQLException e){
            throw new Exception(e);
        }

    }
}
