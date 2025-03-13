package server.handlers.services;

import dataaccess.AuthDAO;

import java.util.UUID;

public class LogoutService {
    //if session handler is faced with a delete request
    //AuthDAO.getAuth to find authToken, then AuthDAO.removeAuth with the provided authToken to delete
    private final AuthDAO authMap;


    public LogoutService(AuthDAO authMap) {
        this.authMap = authMap;
    }
    public Object logout(UUID authToken) throws Exception{
        if(authMap.getAuth(authToken) == null){
            throw new Exception("Error: unauthorized");
        }
        authMap.removeAuth(authToken);
        return null;
    }
}
