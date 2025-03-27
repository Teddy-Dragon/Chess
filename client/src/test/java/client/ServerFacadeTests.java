package client;

import model.GameData;
import org.junit.jupiter.api.*;
import server.Server;
import ui.ClientServerFacade;


public class ServerFacadeTests {

    private static Server server;
    private static ClientServerFacade serverFacade;

    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(0);
        System.out.println("Started test HTTP server on " + port);
        String url = "http://localhost:" + 0;
        serverFacade = new ClientServerFacade(url);
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }


    @Test
    public void sampleTest() {

        Assertions.assertTrue(true);
    }

    @Test
    @DisplayName("Create success")
    public void createSuccess(){
        GameData game = new GameData(0, null, null, "Gamename", null);
        GameData response = serverFacade.createGame(game);
        assert response.gameID() >= 100000;
        assert response.game() != null;
    }


}
