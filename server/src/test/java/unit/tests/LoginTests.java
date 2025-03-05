package unit.tests;

import data.MemoryAuthDAO;
import data.MemoryUserDAO;
import model.AuthData;
import model.UserData;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import server.handlers.services.LoginService;
import server.handlers.services.LogoutService;
import server.handlers.services.UserServices;

import java.util.HashMap;
import java.util.UUID;

public class LoginTests {
    static MemoryUserDAO userMap = new MemoryUserDAO(new HashMap<String, UserData>());
    static MemoryAuthDAO authMap = new MemoryAuthDAO(new HashMap<UUID, AuthData>());
    @AfterEach
    public void cleanUp(){
        userMap.clearAllUsers();
        authMap.clearAllAuth();
    }

    @Test
    @DisplayName("Login Fail- Invalid User")
    public void loginFailInvalidUser(){
        try{
            new LoginService(userMap, authMap).login("Username", "Password");
        }
        catch(Exception e)
        {
            Assertions.assertEquals("Error: unauthorized", e.getMessage());
        }
    }
    @Test
    @DisplayName("Login Fail- Wrong Password")
    public void loginFailWrongPassword(){
        try{
            new UserServices(userMap, authMap).createUser("Username", "Password", "Email");
            Assertions.assertEquals("Password", userMap.getUser("Username").password());
            new LoginService(userMap, authMap).login("Username", "badPassword");
        }
        catch(Exception e)
        {
            Assertions.assertEquals("Error: unauthorized", e.getMessage());
        }
    }

    @Test
    @DisplayName("Login Failed- Missing Params")
    public void loginFailParams(){
        try{
            new UserServices(userMap, authMap).createUser("Username", "Password", "Email");
            Assertions.assertEquals("Password", userMap.getUser("Username").password());
            new LoginService(userMap, authMap).login("Username", null);
        }
        catch(Exception e)
        {
            Assertions.assertEquals("Error: unauthorized", e.getMessage());
        }
    }
    @Test
    @DisplayName("Login Successful")
    public void loginSuccess(){
        try{
            new UserServices(userMap, authMap).createUser("Username", "Password", "Email");
            new UserServices(userMap, authMap).createUser("SecondUser", "SecondPass", "SecondEmail");
            AuthData loginTest = new LoginService(userMap, authMap).login("Username", "Password");
            Assertions.assertEquals("Username", authMap.getAuth(loginTest.authToken()).username());
            AuthData loginTestTwo = new LoginService(userMap, authMap).login("SecondUser", "SecondPass");
            Assertions.assertEquals("SecondUser", authMap.getAuth(loginTestTwo.authToken()).username());
        }
        catch(Exception e)
        {
            assert false;
        }
    }
}
