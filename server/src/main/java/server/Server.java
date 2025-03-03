package server;

import DataAccess.MemoryAuthDAO;
import DataAccess.MemoryGameDAO;
import DataAccess.MemoryUserDAO;
import model.AuthData;
import model.GameData;
import model.UserData;
import spark.*;

import server.handlers.*;

import java.util.HashMap;


public class Server {
    private final MemoryAuthDAO authMap = new MemoryAuthDAO(new HashMap<String, AuthData>());
    private final MemoryGameDAO gameMap = new MemoryGameDAO(new HashMap<Integer, GameData>());
    private final MemoryUserDAO userMap = new MemoryUserDAO(new HashMap<String, UserData>());
    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");


        Spark.post("/user", (request, response) -> new UserHandler().handle(request, response)); //register
        Spark.post("/session", (request, response) -> new SessionHandler().handle(request, response)); //login
        Spark.delete("/session", (request, response) -> new SessionHandler().handle(request, response)); //logout
        Spark.get("/game", (request, response) -> new GameHandler().handle(request, response)); //list games
        Spark.post("/game",(request, response) ->  new GameHandler().handle(request, response)); //create game
        Spark.put("/game",(request, response) ->  new GameHandler().handle(request, response)); //join game
        Spark.delete("/db",(request, response) ->  new ClearHandler().handle(request, response)); //nuke it all


        Spark.awaitInitialization();
        return Spark.port();
    }
    public MemoryAuthDAO getAuthMap(){
        return authMap;
    }
    public MemoryGameDAO getGameMap(){
        return gameMap;
    }
    public MemoryUserDAO getUserMap(){
        return userMap;
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}

