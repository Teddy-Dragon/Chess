package server;

import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryGameDAO;
import dataaccess.MemoryUserDAO;
import model.AuthData;
import model.GameData;
import model.UserData;
import server.handlers.*;
import spark.Spark;

import java.util.HashMap;
import java.util.UUID;
public class Server {
    private AuthDAO authMap;
    private GameDAO gameMap;
    private UserDAO userMap;

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");
        try{
            authMap = new SQLAuthDAO();
            gameMap = new SQLGameDAO();
            userMap = new SQLUserDAO();
        }catch (Exception e){
            System.out.println("Can't connect to Database -> " + e);
        }


        Spark.post("/user", (request, response) -> new UserHandler(userMap, authMap).handle(request, response)); //register
        Spark.post("/session", (request, response) -> new SessionHandler(userMap, authMap).handle(request, response)); //login
        Spark.delete("/session", (request, response) -> new SessionHandler(userMap, authMap).handle(request, response)); //logout
        Spark.get("/game", (request, response) -> new GameHandler(userMap, gameMap, authMap).handle(request, response)); //list games
        Spark.post("/game",(request, response) ->  new GameHandler(userMap, gameMap, authMap).handle(request, response)); //create game
        Spark.put("/game",(request, response) ->  new GameHandler(userMap, gameMap, authMap).handle(request, response)); //join game
        Spark.delete("/db",(request, response) ->  new ClearHandler(authMap, gameMap, userMap).handle(request, response)); //nuke it all
        Spark.post("/play",(request, response) -> new PlayHandler(gameMap, authMap).handle(request, response));

        Spark.awaitInitialization();
        return Spark.port();
    }



    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}

