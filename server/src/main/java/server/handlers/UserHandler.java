package server.handlers;

import com.google.gson.JsonObject;
import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryUserDAO;
import com.google.gson.Gson;
import model.AuthData;
import model.UserData;
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

        try{
            UserData newUserData = new Gson().fromJson(request.body(), UserData.class);
            AuthData newUser = (AuthData) new UserServices(userMap, authMap).createUser(newUserData.username(),
                    newUserData.password(), newUserData.email());
            return new Gson().toJson(newUser);
        }
        catch (Exception e){
            new ExceptionHandler().changeStatus(response, e);
            JsonObject answer = new JsonObject();
            answer.addProperty("message", e.getMessage());
            return answer;
        }

    }
    //only have to worry about post, aka registering
}
