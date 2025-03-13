package dataaccess;

import model.UserData;

public class SQLUserDAO implements UserDAO{
    public void clearAllUsers() {

    }

    public void addUser(String username, UserData userData) {

    }

    public UserData getUser(String username) {
        return null;
    }

    private final String[] createStatements = {
            """
            CREATE TABLE IF NOT EXISTS users(
            'username' varchar(256) NOT NULL,
            'password' varchar(256) NOT NULL,
            'email' varchar(256) NOT NULL,
             PRIMARY KEY('username'),
             INDEX(password),
             INDEX(email)
             )
            
            """
    };
}
