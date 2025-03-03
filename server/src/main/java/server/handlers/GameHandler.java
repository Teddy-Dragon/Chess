package server.handlers;

import DataAccess.MemoryAuthDAO;
import DataAccess.MemoryGameDAO;
import DataAccess.MemoryUserDAO;
import model.GameData;
import server.handlers.services.CreateGameService;
import server.handlers.services.JoinGameService;
import server.handlers.services.ListGamesService;
import spark.Request;
import spark.Response;
import spark.Route;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import com.google.gson.*;

public class GameHandler implements Route {
    private final MemoryUserDAO userMap;
    private final MemoryGameDAO gameMap;
    private final MemoryAuthDAO authMap;

    public GameHandler(MemoryUserDAO userMap, MemoryGameDAO gameMap, MemoryAuthDAO authMap) {
        this.userMap = userMap;
        this.gameMap = gameMap;
        this.authMap = authMap;
    }

    public Object handle(Request request, Response response) throws Exception {
        //check authorization in request and verify with AuthDAO



        if(request.headers("authorization") == null){
            response.status(401);
            return response;
        }
        else{
            if(authMap.getAuth(UUID.fromString(request.headers("authorization"))) == null){
                response.status(401);
                System.out.println("This what get auth is checking for in GameHandler " + authMap.getAuth(UUID.fromString(request.headers("authorization"))));
                return response;
            }
        }

        if(Objects.equals(request.requestMethod(), "POST")){
            CreateGameService createGame = new CreateGameService(userMap, gameMap, authMap);
            GameData body = new Gson().fromJson(request.body(), GameData.class);
            GameData newGame = createGame.makeGame(body.gameName());
            return new Gson().toJson(newGame);
        }
        if(Objects.equals(request.requestMethod(), "PUT")){
           // System.out.println("In PUT");
            JoinGameService join = new JoinGameService(userMap, gameMap, authMap);
             return new Gson().toJson(join);
        }
        if(Objects.equals(request.requestMethod(), "GET")){
            // System.out.println("In GET");
            HashMap<String, List<GameData>> listGames = new ListGamesService(gameMap).ListGames();
            return new Gson().toJson(listGames);
        }
        response.status(405);
        return response;
    }
    //post method means create game
    //get method means list all games
    //put method means join game
}
