package server.handlers;

import server.Server;
import spark.Request;
import spark.Response;
import spark.Route;

public class ClearHandler implements Route {
    public Object handle(Request request, Response response) throws Exception {

        return response;
    }
    //only have to worry about delete, this is the nuke function
    //call AuthDAO.clearAllAuth
    //call UserDAO.clearAllUsers
    //call GameDAO.clearAllGames
    //profit
}
