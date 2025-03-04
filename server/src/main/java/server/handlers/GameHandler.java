package server.handlers;

import data.MemoryAuthDAO;
import data.MemoryGameDAO;
import data.MemoryUserDAO;
import model.GameData;
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


        //Create Game
        if(Objects.equals(request.requestMethod(), "POST")){

            try{
                checkAuth(request, response);
                CreateGameService createGame = new CreateGameService(userMap, gameMap, authMap);
                GameData body = new Gson().fromJson(request.body(), GameData.class);
                GameData newGame = createGame.makeGame(body.gameName());
                return new Gson().toJson(newGame);} catch (Exception e){
                JsonObject answer = new JsonObject();
                answer.addProperty("message", e.getMessage());
                return answer;
            }


        }

        //Join Game
        if(Objects.equals(request.requestMethod(), "PUT")){
           // System.out.println("In PUT");
            JoinRequest joiningUser = new Gson().fromJson(request.body(), JoinRequest.class);

            try{
                checkAuth(request, response);
                Object join = new JoinGameService(userMap, gameMap, authMap)
                        .joinGame(joiningUser.playerColor(), joiningUser.gameID(), currentUser, response);
                if(join == null){
                    return new Gson().toJson(null);
                }
            }catch (Exception e){
                JsonObject answer = new JsonObject();
                answer.addProperty("message", e.getMessage());
                return answer;

            }
        }

        //List Games
        if(Objects.equals(request.requestMethod(), "GET")){
            // System.out.println("In GET");
            HashMap<String, List<GameData>> listGames = new ListGamesService(gameMap).listGames();
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
        if(request.headers("authorization").length() > 36){
            response.status(401);
            throw new Exception("Error: unauthorized");
        }
        else{
            if(authMap.getAuth(UUID.fromString(request.headers("authorization"))) == null){
                response.status(401);
                throw new Exception("Error: unauthorized");
            }
                currentUser = authMap.getAuth(UUID.fromString(request.headers("authorization"))).username();


        }
    }



}
