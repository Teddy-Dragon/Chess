package server.handlers;

import server.handlers.services.LoginService;
import server.handlers.services.LogoutService;
import spark.Request;
import spark.Response;
import spark.Route;

import java.util.Objects;

public class SessionHandler implements Route {
    public Object handle(Request request, Response response){
        if(Objects.equals(request.requestMethod(), "POST")){
            LoginService login = new LoginService();
            System.out.println("In Login");
        }
        if(Objects.equals(request.requestMethod(), "DELETE")){
            //need to read authorization from header
            System.out.println(request.headers());
            LogoutService logout = new LogoutService();
            System.out.println("In Logout");

        }
        return response;
    }

}
