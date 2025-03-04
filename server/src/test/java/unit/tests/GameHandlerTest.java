package unit.tests;

import chess.ChessGame;
import model.GameData;
import org.junit.jupiter.api.*;
import passoff.server.TestServerFacade;
import server.Server;

public class GameHandlerTest {
    private static Server testServer;
    private static TestServerFacade serverFacade;
    private static GameData testGameData;

    @BeforeAll
    public static void init(){
        testServer = new Server();
        var port = testServer.run(8080);

        serverFacade = new TestServerFacade("localhost", Integer.toString(port));
        testGameData = new GameData(123456, null, null, "gameName", new ChessGame());
    }
    @AfterAll
    public static void stop(){
        testServer.stop();
    }

    @Test
    @DisplayName("Successful Game Made")
    public void createGame(){

        assert true;
    }

    @Test
    @DisplayName("Unsuccessful Game Creation- Unauthorized")
    public void createBadGame(){
        assert false;
    }

    @Test
    @DisplayName("Successful Join Game")
    public void joinGameGood(){
        assert true;
    }

    @Test
    @DisplayName("Unsuccessful Join Game")
    public void joinGameBad(){
        assert false;
    }

    @Test
    @DisplayName("Successfully List Games")
    public void listGameGood(){
        assert true;
    }

    @Test
    @DisplayName("Unsuccessfully List Games")
    public void listGameBad(){
        assert false;
    }

}
