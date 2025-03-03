package DataAccess;

import model.UserData;

public class UserDAO {
    public void clearAllUsers(){
        //clears all User data from database
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
