package server.handlers;

import server.handlers.services.CreateGameService;
import server.handlers.services.JoinGameService;
import server.handlers.services.ListGamesService;
import spark.Request;
import spark.Response;
import spark.Route;

import java.util.Objects;

public class GameHandler implements Route {
    public Object handle(Request request, Response response) throws Exception {
        //check authorization in request and verify with AuthDAO
        if(Objects.equals(request.requestMethod(), "POST")){
            System.out.println("In POST \n");
            CreateGameService createGame = new CreateGameService();
            return createGame;
        }
        if(Objects.equals(request.requestMethod(), "PUT")){
            System.out.println("In PUT");
            JoinGameService join = new JoinGameService();
             return join;
        }
        if(Objects.equals(request.requestMethod(), "GET")){
            System.out.println("In GET");
            ListGamesService listGames = new ListGamesService();
            return listGames;
        }
        return response;
    }
    //post method means create game
    //get method means list all games
    //put method means join game
}
