package server.handlers.services;

import DataAccess.MemoryAuthDAO;
import DataAccess.MemoryUserDAO;
import model.AuthData;
import model.UserData;

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

    public Object checkUsername(String username){
        return userMap.getUser(username);

    }

    public Object createUser(String username, String password, String email){
        //checkUsername();
        if(checkUsername(username) != null){
            return null;
        }
        UserData newUser = new UserData(username, password, email);
        userMap.addUser(username, newUser);
        UUID authToken = new CreateAuth(authMap).newToken();
        AuthData newAuth = new AuthData(authToken, username);
        authMap.addAuth(authToken, newAuth);

        return newAuth;

    }
}
