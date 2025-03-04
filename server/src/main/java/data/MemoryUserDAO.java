package data;

import model.UserData;

import java.util.HashMap;

public class MemoryUserDAO{

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
//    public void updateUser(String username, UserData newUserData){
//        // will update userdata i.e password or username changes
//        UserData oldData = userMap.get(username);
//        userMap.remove(username, oldData);
//        userMap.put(username, newUserData);
//
//    }
//    public void deleteUser(String username){
//        UserData data = userMap.get(username);
//        userMap.remove(username, data);
//    }

}
