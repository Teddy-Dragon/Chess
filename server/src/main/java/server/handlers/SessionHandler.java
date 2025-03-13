package server.handlers;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import dataaccess.AuthDAO;
import dataaccess.UserDAO;
import model.AuthData;
import model.UserData;
import server.handlers.services.LoginService;
import server.handlers.services.LogoutService;
import spark.Request;
import spark.Response;
import spark.Route;

import java.util.Objects;
import java.util.UUID;

public class SessionHandler implements Route {
    private final AuthDAO authMap;
    private final UserDAO userMap;

    public SessionHandler(UserDAO userMap, AuthDAO authMap) {
        this.authMap = authMap;
        this.userMap = userMap;
    }

    public Object handle(Request request, Response response){
        //login
        if(Objects.equals(request.requestMethod(), "POST")){
            UserData loginInfo = new Gson().fromJson(request.body(), UserData.class);
            try{
                AuthData login = new LoginService(userMap, authMap).login(loginInfo.username(), loginInfo.password());
                return new Gson().toJson(login);
            }catch (Exception e){
                new ExceptionHandler().changeStatus(response, e);
                JsonObject answer = new JsonObject();
                answer.addProperty("message", e.getMessage());
                return answer;
            }

        }
        //logout
        if(Objects.equals(request.requestMethod(), "DELETE")){
            UUID authToken = UUID.fromString(request.headers("authorization"));
            try{Object logout = new LogoutService(authMap).logout(authToken);
                return new Gson().toJson(logout);}
            catch (Exception e){
                new ExceptionHandler().changeStatus(response, e);
                JsonObject answer = new JsonObject();
                answer.addProperty("message", e.getMessage());
                return answer;
            }


        }
        response.status(405);
        return response;
    }


}
