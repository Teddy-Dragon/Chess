package server.handlers;

import DataAccess.MemoryAuthDAO;
import DataAccess.MemoryGameDAO;
import DataAccess.MemoryUserDAO;
import model.GameData;
import model.IncorrectResponse;
import model.JoinRequest;
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
    private String currentUser = null;

    public GameHandler(MemoryUserDAO userMap, MemoryGameDAO gameMap, MemoryAuthDAO authMap) {
        this.userMap = userMap;
        this.gameMap = gameMap;
        this.authMap = authMap;
    }

    public Object handle(Request request, Response response) throws Exception {
        //check authorization in request and verify with AuthDAO
        checkAuth(request, response);
        //Create Game
        if(Objects.equals(request.requestMethod(), "POST")){
            CreateGameService createGame = new CreateGameService(userMap, gameMap, authMap);
            GameData body = new Gson().fromJson(request.body(), GameData.class);
            GameData newGame = createGame.makeGame(body.gameName());
            return new Gson().toJson(newGame);
        }

        //Join Game
        if(Objects.equals(request.requestMethod(), "PUT")){
           // System.out.println("In PUT");
            JoinRequest joiningUser = new Gson().fromJson(request.body(), JoinRequest.class);
            IncorrectResponse join = new JoinGameService(userMap, gameMap, authMap).joinGame(joiningUser.playerColor(), joiningUser.gameID(), currentUser);
            if(join == null){
                return new Gson().toJson(null);
            }
            else {
                if(join.alreadyTaken()){
                    response.status(403);
                    throw new Exception("Error: already taken");
                }
                if(join.badRequest()){
                    response.status(400);
                    throw new Exception("Error: bad request");
                }
                if(join.unauthorized()){
                    response.status(401);
                    throw new Exception("Error: unauthorized");
                }
                if(join.ErrorMessage() != null){
                    throw new Exception(join.ErrorMessage());
                }
            }
        }

        //List Games
        if(Objects.equals(request.requestMethod(), "GET")){
            // System.out.println("In GET");
            HashMap<String, List<GameData>> listGames = new ListGamesService(gameMap).ListGames();
            return new Gson().toJson(listGames);
        }
        response.status(405);
        return response;
    }
    public void checkAuth(Request request, Response response) throws Exception{
        if(request.headers("authorization") == ""){
            response.status(401);
            throw new Exception("Error: unauthorized");
        }
        else{
            if(authMap.getAuth(UUID.fromString(request.headers("authorization"))) == null){
                response.status(401);
                throw new Exception("Error: unauthorized- invalid authToken");
            }
            currentUser = authMap.getAuth(UUID.fromString(request.headers("authorization"))).username();

        }
    }

}
