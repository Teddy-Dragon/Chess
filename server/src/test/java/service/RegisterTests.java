package service;

import dataaccess.AuthDAO;
import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryUserDAO;
import dataaccess.UserDAO;
import model.AuthData;
import model.UserData;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mindrot.jbcrypt.BCrypt;
import server.handlers.services.UserServices;

import java.util.HashMap;
import java.util.UUID;

public class RegisterTests {
    static UserDAO userMap = new MemoryUserDAO(new HashMap<String, UserData>());
    static AuthDAO authMap = new MemoryAuthDAO(new HashMap<UUID, AuthData>());
    @AfterEach
    public void cleanUp(){
        userMap.clearAllUsers();
        authMap.clearAllAuth();
    }

    @Test
    @DisplayName("Register Fail, Username Taken")
    public void registerFailUsername(){

        try{
            new UserServices(userMap, authMap).createUser("Username", "Password", "Email");
            new UserServices(userMap, authMap).createUser("Username", "Different_Password", "Email");
        }
        catch(Exception e){
            Assertions.assertEquals("Error: already taken", e.getMessage());
        }
    }

    @Test
    @DisplayName("Register Fail- Missing Params")
    public void registerFailParam(){
        try{
            new UserServices(userMap, authMap).createUser("Username", null, "Email");
        }
        catch(Exception e){
            Assertions.assertEquals("Error: bad request", e.getMessage());
        }

    }

    @Test
    @DisplayName("Register Successful")
    public void registerSuccess(){
        try{
            new UserServices(userMap, authMap).createUser("Username", "notnull", "Email");
            new UserServices(userMap, authMap).createUser("anotherUser", "Password2", "Email2");
            assert BCrypt.checkpw("notnull", userMap.getUser("Username").password());
            Assertions.assertEquals("Email", userMap.getUser("Username").email());
            assert BCrypt.checkpw("Password2", userMap.getUser("anotherUser").password());
            Assertions.assertEquals("Email2", userMap.getUser("anotherUser").email());
        }
        catch(Exception e){
            assert false;
        }

    }

}
