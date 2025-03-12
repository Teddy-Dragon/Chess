package service;

import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryUserDAO;
import model.AuthData;
import model.UserData;
import org.junit.jupiter.api.*;
import server.handlers.services.UserServices;

import java.util.HashMap;
import java.util.UUID;

public class RegisterTests {
    static MemoryUserDAO userMap = new MemoryUserDAO(new HashMap<String, UserData>());
    static MemoryAuthDAO authMap = new MemoryAuthDAO(new HashMap<UUID, AuthData>());
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
            Assertions.assertEquals("notnull", userMap.getUser("Username").password());
            Assertions.assertEquals("Email", userMap.getUser("Username").email());
            Assertions.assertEquals("Password2", userMap.getUser("anotherUser").password());
            Assertions.assertEquals("Email2", userMap.getUser("anotherUser").email());
        }
        catch(Exception e){
            assert false;
        }

    }

}
