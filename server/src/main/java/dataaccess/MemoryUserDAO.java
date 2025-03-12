package dataaccess;

import model.UserData;

import java.util.HashMap;

public class MemoryUserDAO implements UserDAO{

    private final HashMap<String, UserData> userMap;

    public MemoryUserDAO(HashMap<String, UserData> userMap) {
        this.userMap = userMap;
    }

    public void clearAllUsers(){
        userMap.clear();
    }
    public void addUser(String username, UserData userData){
        //puts not previously existing UserData in database
        userMap.put(username, userData);
    }
    public UserData getUser(String username){
        //void temporarily, will return user data from database
        return userMap.get(username);
    }


}
