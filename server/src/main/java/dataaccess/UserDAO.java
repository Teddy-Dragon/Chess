package dataaccess;

import model.UserData;

public interface UserDAO {
    public void clearAllUsers();
    public void addUser(UserData userData);
    public UserData getUser(String username);
}
