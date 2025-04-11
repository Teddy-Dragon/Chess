package server.handlers;

import com.google.gson.Gson;
import dataaccess.AuthDAO;
import dataaccess.GameDAO;
import model.GameData;
import model.JoinRequest;
import spark.Request;
import spark.Response;
import spark.Route;

import java.util.Objects;
import java.util.UUID;

public class PlayHandler implements Route {
    private final GameDAO gameMap;
    private final AuthDAO authMap;


    public PlayHandler(GameDAO gameMap, AuthDAO authMap) {
        this.gameMap = gameMap;
        this.authMap = authMap;
    }

    public Object handle(Request request, Response response) throws Exception {
        if (authMap.getAuth(UUID.fromString(request.headers("authorization"))) == null) {
            response.status(401);
            throw new Exception("Error: unauthorized");
        }

        if (Objects.equals(request.requestMethod(), "POST")) {

            int gameID = new Gson().fromJson(request.body(), Integer.class);

            GameData databaseResponse = verifyExist(gameID, response);
            return new Gson().toJson(databaseResponse);
        }
        if(Objects.equals(request.requestMethod(), "PUT")){
            JoinRequest playerRequest = new Gson().fromJson(request.body(), JoinRequest.class);
            GameData databaseResponse = verifyExist(playerRequest.gameID(), response);
            if(Objects.equals(playerRequest.playerColor(), "white")){
                GameData newGame = new GameData(playerRequest.gameID(), null,
                        databaseResponse.blackUsername(), databaseResponse.gameName(), databaseResponse.game());
                gameMap.updateGame(playerRequest.gameID(), newGame);
                GameData updatedGame = gameMap.getGameByID(playerRequest.gameID());
                return new Gson().toJson(updatedGame);
            }
            if(Objects.equals(playerRequest.playerColor(), "black")){
                GameData newGame = new GameData(playerRequest.gameID(), databaseResponse.whiteUsername(),
                        null, databaseResponse.gameName(), databaseResponse.game());
                gameMap.updateGame(playerRequest.gameID(), newGame);
                GameData updatedGame = gameMap.getGameByID(playerRequest.gameID());
                return new Gson().toJson(updatedGame);
            }
        }
        return null;
    }


    private GameData verifyExist(int gameID, Response response) throws Exception{
        GameData databaseResponse = gameMap.getGameByID(gameID);
        if (databaseResponse == null) {
            response.status(404);
            throw new Exception("Error: Game doesn't exist");
        }
        return databaseResponse;
    }
}