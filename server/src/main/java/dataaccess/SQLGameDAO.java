package dataaccess;

import chess.ChessGame;
import com.google.gson.Gson;
import model.GameData;

import java.util.HashMap;
import java.util.List;

public class SQLGameDAO implements GameDAO{
    public SQLGameDAO() throws Exception{
        String[] createStatements = {
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
        new DatabaseManager().configureDatabase(createStatements);
    }
    public void clearAllGames() {
        String deleteStatements = "TRUNCATE TABLE game";
        try(var conn = DatabaseManager.getConnection()){
            try(var preparedStatement = conn.prepareStatement(deleteStatements)){
                preparedStatement.executeUpdate();
            }
        }catch(Exception ignored){

        }

    }

    public void addGame(GameData gameData) {
        String statement = "INSERT INTO game (gameID, whiteUsername, blackUsername, gameName, game) VALUES (?, null, null, ?, ?)";
        Gson gson = new Gson();
        var json = gson.toJson(gameData.game());


        try(var conn = DatabaseManager.getConnection()){
            try(var preparedStatement = conn.prepareStatement(statement)){
                preparedStatement.setInt(1, gameData.gameID());
                preparedStatement.setString(2, gameData.gameName());
                preparedStatement.setString(3, json);
                System.out.println(preparedStatement);
                preparedStatement.executeUpdate();
            }
        }catch(Exception e){
            System.out.println(e);
        }


    }

    public GameData getGameByID(int gameID) {
        String statement = "SELECT gameID, whiteUsername, blackUsername, gameName, game FROM game WHERE gameID=?";
        Gson gson = new Gson();
        ChessGame game;
        try(var conn = DatabaseManager.getConnection()){
            try(var preparedStatement = conn.prepareStatement(statement)){
                preparedStatement.setInt(1, gameID);
                try(var data = preparedStatement.executeQuery()){
                    if(data.next()){
                        game = gson.fromJson(data.getString("game"), ChessGame.class);
                        return new GameData(gameID, data.getString("whiteUsername"),
                                data.getString("blackUsername"), data.getString("gameName"),game );


                    }
                }
            }
        }catch(Exception e){
            System.out.println(e);
        }
        return null;
    }

    public HashMap<String, List<GameData>> getAllGames() {
        return null;
    }

    public void updateGame(int gameID, GameData newGameData) {
        String statement = "UPDATE game SET whiteUsername=?, blackUsername=?, gameName=?, game=? WHERE gameID=?";
        Gson gson = new Gson();
        var json = gson.toJson(newGameData.game());
        try(var conn = DatabaseManager.getConnection()){
            try(var preparedStatement = conn.prepareStatement(statement)){
                preparedStatement.setString(1, newGameData.whiteUsername());
                preparedStatement.setString(2, newGameData.blackUsername());
                preparedStatement.setString(3, newGameData.gameName());
                preparedStatement.setString(4, json);
                preparedStatement.setInt(5, gameID);
                preparedStatement.executeUpdate();
            }

        }catch(Exception e){
            System.out.println(e);
        }

    }


}
