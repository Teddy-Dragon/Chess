package unit.tests;

import data.MemoryAuthDAO;
import data.MemoryGameDAO;
import data.MemoryUserDAO;
import model.AuthData;
import model.GameData;
import model.UserData;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import server.handlers.services.CreateGameService;
import server.handlers.services.JoinGameService;
import server.handlers.services.LoginService;
import spark.Response;
import spark.ResponseTransformer;

import java.util.HashMap;
import java.util.UUID;

public class JoinServiceTests {

    MemoryUserDAO userMap = new MemoryUserDAO(new HashMap<String, UserData>());
    MemoryGameDAO gameMap = new MemoryGameDAO(new HashMap<Integer, GameData>());
    MemoryAuthDAO authMap = new MemoryAuthDAO(new HashMap<UUID, AuthData>());


    @Test
    @DisplayName("Fail to join- White already taken")
    public void joinFailWhite(){

        try {
            GameData testGame = new CreateGameService(userMap, gameMap, authMap).makeGame("testGame");


        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    @DisplayName("Fail To Join- Black Taken")
    public void joinFailBlack(){
        assert true;
    }

    @Test
    @DisplayName("Fail To Join- Game Doesn't Exist")
    public void joinFailGame(){
        assert true;

    }
}
