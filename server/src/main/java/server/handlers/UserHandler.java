package server.handlers;

import DataAccess.MemoryAuthDAO;
import DataAccess.MemoryUserDAO;
import com.google.gson.Gson;
import model.AuthData;
import model.UserData;
import server.handlers.services.CreateGameService;
import server.handlers.services.UserServices;
import spark.Request;
import spark.Response;
import spark.Route;

public class UserHandler implements Route {
    private final MemoryUserDAO userMap;
    private final MemoryAuthDAO authMap;

    public UserHandler(MemoryUserDAO userMap, MemoryAuthDAO authMap) {
        this.userMap = userMap;
        this.authMap = authMap;
    }

    public Object handle(Request request, Response response){
        UserData newUserData = new Gson().fromJson(request.body(), UserData.class);
        try{AuthData newUser = (AuthData) new UserServices(userMap, authMap).createUser(newUserData.username(), newUserData.password(), newUserData.email(), response);
            return new Gson().toJson(newUser);}
        catch (Exception e){
            return new Gson().toJson(e.getMessage());
        }

    }
    //only have to worry about post, aka registering
}
