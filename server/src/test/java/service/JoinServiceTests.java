package service;

import dataaccess.*;
import model.AuthData;
import model.GameData;
import model.UserData;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import server.handlers.services.CreateGameService;
import server.handlers.services.JoinGameService;

import java.util.HashMap;
import java.util.UUID;

public class JoinServiceTests {

    static UserDAO userMap = new MemoryUserDAO(new HashMap<String, UserData>());
    static GameDAO gameMap = new MemoryGameDAO(new HashMap<Integer, GameData>());
    static AuthDAO authMap = new MemoryAuthDAO(new HashMap<UUID, AuthData>());

    @AfterAll
    public static void cleanUp(){
        userMap.clearAllUsers();
        gameMap.clearAllGames();
        authMap.clearAllAuth();
    }

    @Test
    @DisplayName("Fail to join- White already taken")
    public void joinFailWhite(){

        try {
            GameData testGame = new CreateGameService(gameMap).makeGame("testGame");
            new JoinGameService(userMap, gameMap, authMap).joinGame("WHITE", testGame.gameID(), "OriginalUser");
            new JoinGameService(userMap, gameMap, authMap).joinGame("WHITE", testGame.gameID(), "OtherUser");


        } catch (Exception e) {
            Assertions.assertEquals("Error: already taken", e.getMessage());

        }
    }

    @Test
    @DisplayName("Fail To Join- Black Taken")
    public void joinFailBlack()  {
        try{
            GameData testGame = new CreateGameService(gameMap).makeGame("testGame");
            new JoinGameService(userMap, gameMap, authMap).joinGame("BLACK", testGame.gameID(), "OriginalUser");
            new JoinGameService(userMap, gameMap, authMap).joinGame("BLACK", testGame.gameID(), "OtherUser");
        }catch(Exception e){
            Assertions.assertEquals("Error: already taken", e.getMessage());
        }

    }

    @Test
    @DisplayName("Fail To Join- Game Doesn't Exist")
    public void joinFailGame(){
        try{
            new JoinGameService(userMap, gameMap, authMap).joinGame("BLACK", 200000, "OriginalUser");
        }
        catch(Exception e){
            Assertions.assertEquals("Error: bad request", e.getMessage());
        }

    }
    @Test
    @DisplayName("Successfully Joined")

    public void joinSuccess(){
        try{
            GameData testGame = new CreateGameService(gameMap).makeGame("testGame");
            new JoinGameService(userMap, gameMap, authMap).joinGame("BLACK", testGame.gameID(), "OriginalUser");
            new JoinGameService(userMap, gameMap, authMap).joinGame("WHITE", testGame.gameID(), "OtherUser");
            Assertions.assertEquals( "OriginalUser", gameMap.getGameByID(testGame.gameID()).blackUsername());
            Assertions.assertEquals("OtherUser", gameMap.getGameByID(testGame.gameID()).whiteUsername());

        }catch(Exception e) {
            assert false;
        }

    }
}
