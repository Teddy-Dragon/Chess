package DataAccess;

import model.UserData;

import java.util.HashMap;

public class MemoryUserDAO implements UserDAO {

    private final HashMap<String, UserData> userMap;

    public MemoryUserDAO(HashMap<String, UserData> userMap) {
        this.userMap = userMap;
    }

    public void clearAllUsers(){
        userMap.clear();
    }
    public void addUser(){
        //puts not previously existing UserData in database
    }
    public UserData getUser(String username){
        //void temporarily, will return user data from database
        return null;
    }
    public void updateUser(String username){
        // will update userdata i.e password or username changes
    }
    public void deleteUser(String username){
        // deletes one user
    }

}
