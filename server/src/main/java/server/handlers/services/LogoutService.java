package server.handlers.services;

import data.MemoryAuthDAO;
import data.MemoryUserDAO;
import spark.Response;

import java.util.UUID;

public class LogoutService {
    //if session handler is faced with a delete request
    //AuthDAO.getAuth to find authToken, then AuthDAO.removeAuth with the provided authToken to delete
    private final MemoryUserDAO userMap;
    private final MemoryAuthDAO authMap;


    public LogoutService(MemoryUserDAO userMap, MemoryAuthDAO authMap) {
        this.userMap = userMap;
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
