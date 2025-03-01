package server.handlers;

import spark.Request;
import spark.Response;
import spark.Route;

public class SessionHandler implements Route {
    public Object handle(Request request, Response response){
        return null;
    }

    //post method means Login- refer to LoginService
    //delete method means logout- reger to LogoutService
}
