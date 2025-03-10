package server.handlers.services;

import data.MemoryAuthDAO;
import data.MemoryUserDAO;
import model.AuthData;
import model.UserData;
import spark.Response;

import java.util.UUID;

public class UserServices {
    //UserHandler will refer to this regardless of method, this is just to make it nice and separate

    //Call UserDAO.getUser, if it returns null, create authToken and send UserData model to UserDAO.createUser
    private final MemoryUserDAO userMap;
    private final MemoryAuthDAO authMap;

    public UserServices(MemoryUserDAO userMap, MemoryAuthDAO authMap) {
        this.userMap = userMap;
        this.authMap = authMap;
    }

    private Object checkUsername(String username){
        return userMap.getUser(username);

    }

    public AuthData createUser(String username, String password, String email) throws Exception{
        //checkUsername();
        if(checkUsername(username) != null){
            throw new Exception("Error: already taken");
        }
        if(username == null || password == null || email == null){
            throw new Exception("Error: bad request");

        }
        UserData newUser = new UserData(username, password, email);
        userMap.addUser(username, newUser);
        UUID authToken = new CreateAuth(authMap).newToken();
        AuthData newAuth = new AuthData(authToken, username);
        authMap.addAuth(authToken, newAuth);

        return newAuth;

    }
}
