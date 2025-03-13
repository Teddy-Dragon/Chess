package server.handlers;

import dataaccess.*;
import spark.Request;
import spark.Response;
import spark.Route;
import com.google.gson.Gson;

public class ClearHandler implements Route {
    private final AuthDAO authMap;
    private final GameDAO gameMap;
    private final UserDAO userMap;
    public ClearHandler(AuthDAO authMap, GameDAO gameMap, UserDAO userMap) {
        this.authMap = authMap;
        this.gameMap = gameMap;
        this.userMap = userMap;

    }

    public Object handle(Request request, Response response) throws Exception {
        userMap.clearAllUsers();
        gameMap.clearAllGames();
        authMap.clearAllAuth();



        return new Gson().toJson(null);
    }
    //only have to worry about delete, this is the nuke function
    //call AuthDAO.clearAllAuth
    //call UserDAO.clearAllUsers
    //call GameDAO.clearAllGames
    //profit
}
