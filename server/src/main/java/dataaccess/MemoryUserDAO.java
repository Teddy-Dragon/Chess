package dataaccess;

import model.UserData;
import org.mindrot.jbcrypt.BCrypt;

import java.util.HashMap;

public class MemoryUserDAO implements UserDAO{

    private final HashMap<String, UserData> userMap;

    public MemoryUserDAO(HashMap<String, UserData> userMap) {
        this.userMap = userMap;
    }

    public void clearAllUsers(){
        userMap.clear();
    }
    public void addUser(UserData userData){
        UserData encrypt = new UserData(userData.username(), BCrypt.hashpw(userData.password(),
                BCrypt.gensalt()), userData.email());
        //puts not previously existing UserData in database
        userMap.put(userData.username(), encrypt);
    }
    public UserData getUser(String username){
        //void temporarily, will return user data from database
        return userMap.get(username);
    }


}
