package server.handlers.services;

import data.MemoryAuthDAO;
import data.MemoryUserDAO;
import model.AuthData;
import model.UserData;
import spark.Response;

import java.util.Objects;
import java.util.UUID;

public class LoginService {
    private final MemoryUserDAO userMap;
    private final MemoryAuthDAO authMap;

    public LoginService(MemoryUserDAO userMap, MemoryAuthDAO authMap) {
        this.userMap = userMap;
        this.authMap = authMap;
    }
    public AuthData login(String username, String password) throws Exception{
        UserData returningUser = userMap.getUser(username);
        if(returningUser != null) {
            if(Objects.equals(returningUser.password(), password)){
                UUID authToken = new CreateAuth(authMap).newToken();
                AuthData authData = new AuthData(authToken, username);
                authMap.addAuth(authToken, authData);
                return authData;
            }
            else {
                throw new Exception("Error: unauthorized");
            }
        }
        else{
            throw new Exception("Error: unauthorized");
        }



    }
    //if session handler is faced with a post request
    //UserDAO.getUser with the username provided. If passwords then match that username, you win
    //generate authToken and add with AuthDAO.addAuth

}
