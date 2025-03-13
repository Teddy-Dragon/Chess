package service;

import dataaccess.AuthDAO;
import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryUserDAO;
import model.AuthData;
import model.UserData;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import server.handlers.services.LogoutService;
import server.handlers.services.UserServices;

import java.util.HashMap;
import java.util.UUID;

public class MemoryLogoutTests {
    static AuthDAO authMap = new MemoryAuthDAO(new HashMap<UUID, AuthData>());
    @AfterEach
    public void cleanUp(){
        authMap.clearAllAuth();
    }

    @Test
    @DisplayName("Logout Failed- Not Logged In")
    public void logoutFailNoUser(){
        try{
            new LogoutService(authMap).logout(null);
        }catch(Exception e){
            Assertions.assertEquals("Error: unauthorized", e.getMessage());
        }
    }

    @Test
    @DisplayName("Logout Failed- Invalid Auth")
    public void logoutFailInvalidAuth(){
        try{
            new LogoutService(authMap).logout(UUID.randomUUID());
        } catch (Exception e) {
            Assertions.assertEquals("Error: unauthorized", e.getMessage());
        }
    }

    @Test
    @DisplayName("Successful Logout")
    public void logoutSuccess(){
        try{
           AuthData user = new UserServices(new MemoryUserDAO(new HashMap<String, UserData>()), authMap).createUser("Username", "Password", "Email");
           Assertions.assertEquals(user.username(), authMap.getAuth(user.authToken()).username());
           new LogoutService(authMap).logout(user.authToken());
           Assertions.assertNull(authMap.getAuth(user.authToken()));
        }
        catch(Exception e)
        {
            assert false;
        }

    }
}
