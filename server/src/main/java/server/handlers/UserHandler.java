package server.handlers;

import server.handlers.services.CreateGameService;
import spark.Request;
import spark.Response;
import spark.Route;

public class UserHandler implements Route {
    public Object handle(Request request, Response response){
        System.out.println(request.body() + "\n");
        System.out.println(request.attributes()+ "\n");
        System.out.println(request.contentType()+"\n");
        System.out.println(request.contextPath()+"\n");
        System.out.println(request.headers()+"\n");
        System.out.println(request.requestMethod() + "\n");
        System.out.println(response.body());
        return response;
    }
    //only have to worry about post, aka registering
}
