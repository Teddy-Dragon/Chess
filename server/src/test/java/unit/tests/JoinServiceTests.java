package unit.tests;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import server.handlers.services.JoinGameService;
import server.handlers.services.LoginService;

public class JoinServiceTests {




    @Test
    @DisplayName("Fail to join- White already taken")
    public void joinFailWhite(){

        assert true;
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
