package dataaccess;

import chess.ChessGame;
import com.google.gson.Gson;
import model.GameData;

import java.util.ArrayList;
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

    public  HashMap<String,List<GameData>> getAllGames() {
        HashMap<String, List<GameData>> allGames = new HashMap<String, List<GameData>>();
        List<GameData> games = new ArrayList<>();
        String statement = "SELECT * FROM game";
        try(var conn = DatabaseManager.getConnection()){
            try(var prepareStatement = conn.prepareStatement(statement)){
                try(var data = prepareStatement.executeQuery()){
                    while(data.next()){
                        Gson gson = new Gson();
                        ChessGame game = gson.fromJson(data.getString("game"), ChessGame.class);
                        GameData response = new GameData(data.getInt("gameID"),
                                data.getString("whiteUsername"), data.getString("blackUsername"),
                                data.getString("gameName"), game);
                        games.add(response);

                    }

                }
                allGames.put("games", games);
                return allGames;
            }

        }
        catch (Exception e){
            System.out.println(e);
            return null;
        }

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
