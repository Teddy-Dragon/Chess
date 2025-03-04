package server.handlers;

import DataAccess.MemoryAuthDAO;
import DataAccess.MemoryGameDAO;
import DataAccess.MemoryUserDAO;
import model.AuthData;
import spark.Request;
import spark.Response;
import spark.Route;
import com.google.gson.Gson;

public class ClearHandler implements Route {
    private final MemoryAuthDAO authMap;
    private final MemoryGameDAO gameMap;
    private final MemoryUserDAO userMap;
    public ClearHandler(MemoryAuthDAO authMap, MemoryGameDAO gameMap, MemoryUserDAO userMap) {
        this.authMap = authMap;
        this.gameMap = gameMap;
        this.userMap = userMap;

    }

    public Object handle(Request request, Response response) throws Exception {
        authMap.clearAllAuth();
        gameMap.clearAllGames();
        userMap.clearAllUsers();



        return new Gson().toJson(null);
    }
    //only have to worry about delete, this is the nuke function
    //call AuthDAO.clearAllAuth
    //call UserDAO.clearAllUsers
    //call GameDAO.clearAllGames
    //profit
}
