package server.handlers.services;

import dataaccess.AuthDAO;
import dataaccess.UserDAO;
import model.AuthData;
import model.UserData;

import java.util.Objects;
import java.util.UUID;

public class LoginService {
    private final UserDAO userMap;
    private final AuthDAO authMap;

    public LoginService(UserDAO userMap, AuthDAO authMap) {
        this.userMap = userMap;
        this.authMap = authMap;
    }
    public AuthData login(String username, String password) throws Exception{
        UserData returningUser = userMap.getUser(username);
        if(returningUser != null) {
            if(Objects.equals(returningUser.password(), password)){
                UUID authToken = new CreateAuth(authMap).newToken();
                AuthData authData = new AuthData(authToken, username);
                authMap.addAuth(authData);
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
