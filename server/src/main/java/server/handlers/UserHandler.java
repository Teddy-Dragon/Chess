package server.handlers;

import server.handlers.services.CreateGameService;
import spark.Request;
import spark.Response;
import spark.Route;

public class UserHandler implements Route {
    public Object handle(Request request, Response response){
        return response;
    }
    //only have to worry about post, aka registering
}
