package server.handlers;

import com.google.gson.Gson;
import dataaccess.AuthDAO;
import dataaccess.GameDAO;
import model.GameData;
import spark.Request;
import spark.Response;
import spark.Route;

import java.util.UUID;

public class PlayHandler implements Route {
    private final GameDAO gameMap;
    private final AuthDAO authMap;


    public PlayHandler(GameDAO gameMap, AuthDAO authMap) {
        this.gameMap = gameMap;
        this.authMap = authMap;
    }

    public Object handle(Request request, Response response) throws Exception {
        int gameID = new Gson().fromJson(request.body(), Integer.class);
        if(authMap.getAuth(UUID.fromString(request.headers("authorization"))) == null){
            response.status(401);
            throw new Exception("Error: unauthorized");
        }
        GameData databaseResponse = gameMap.getGameByID(gameID);
        if(databaseResponse == null){
            response.status(404);
            throw new Exception("Error: Game doesn't exist");
        }

        return new Gson().toJson(databaseResponse);

    }
}