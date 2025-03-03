package server.handlers;

import DataAccess.MemoryAuthDAO;
import DataAccess.MemoryUserDAO;
import com.google.gson.Gson;
import model.AuthData;
import model.UserData;
import org.eclipse.jetty.server.Authentication;
import server.handlers.services.LoginService;
import server.handlers.services.LogoutService;
import spark.Request;
import spark.Response;
import spark.Route;

import java.util.Objects;
import java.util.UUID;

public class SessionHandler implements Route {
    private final MemoryAuthDAO authMap;
    private final MemoryUserDAO userMap;

    public SessionHandler(MemoryUserDAO userMap, MemoryAuthDAO authMap) {
        this.authMap = authMap;
        this.userMap = userMap;
    }

    public Object handle(Request request, Response response){
        if(Objects.equals(request.requestMethod(), "POST")){
            UserData loginInfo = new Gson().fromJson(request.body(), UserData.class);
            AuthData login = new LoginService(userMap, authMap).login(loginInfo.username(), loginInfo.password());
            return new Gson().toJson(login);
        }
        if(Objects.equals(request.requestMethod(), "DELETE")){
            UUID authToken = UUID.fromString(request.headers("authorization"));
            Object logout = new LogoutService(userMap, authMap).logout(authToken);
            return new Gson().toJson(logout);

        }
        response.status(405);
        return response;
    }

}
