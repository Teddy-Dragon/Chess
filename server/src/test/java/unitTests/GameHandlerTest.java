package unitTests;

import org.junit.jupiter.api.*;
import passoff.model.TestAuthResult;
import passoff.model.TestCreateRequest;
import passoff.model.TestUser;
import passoff.server.TestServerFacade;
import server.Server;

public class GameHandlerTest {
    private static TestUser existingUser;

    private static TestUser newUser;

    private static TestCreateRequest createRequest;

    private static TestServerFacade serverFacade;
    private static Server server;

    private String existingAuth;
    @AfterAll
    static void stopServer() {
        server.stop();
    }


    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(0);
        System.out.println("Started test HTTP server on " + port);

        serverFacade = new TestServerFacade("localhost", Integer.toString(port));

        existingUser = new TestUser("ExistingUser", "existingUserPassword", "eu@mail.com");

        newUser = new TestUser("NewUser", "newUserPassword", "nu@mail.com");

        createRequest = new TestCreateRequest("testGame");
    }

    @BeforeEach
    public void setup() {
        serverFacade.clear();

        //one user already logged in
        TestAuthResult regResult = serverFacade.register(existingUser);
        existingAuth = regResult.getAuthToken();
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
    @DisplayName("Unsuccessful Game Creation- ")
    public void createBadGameTwo(){
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
