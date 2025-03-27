package client;

import model.GameData;
import model.JoinRequest;
import model.ListModel;
import model.UserData;
import org.junit.jupiter.api.*;
import server.Server;
import ui.ClientServerFacade;

import java.util.Objects;


public class ServerFacadeTests {

    private static Server server;
    private static ClientServerFacade serverFacade;

    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(0);
        System.out.println("Started test HTTP server on " + port);
        String url = "http://localhost:" + port;
        serverFacade = new ClientServerFacade(url);
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }

    @BeforeEach
    public void setUp(){
        try{
            serverFacade.clearAll();
        }catch(Exception e){
            System.out.println("Failure to clear database");
        }
    }

    @Test
    public void sampleTest() {

        Assertions.assertTrue(true);
    }

    @Test
    @DisplayName("Create success")
    public void createSuccess(){
        UserData registerUser = new UserData("Test", "password", "email@email.com");

        GameData game = new GameData(0, null, null, "Gamename", null);
        try{
            serverFacade.registerUser(registerUser);
            GameData response = serverFacade.createGame(game);
            assert response.gameID() >= 100000;
            assert response.game() != null;
        }catch(Exception e){
            assert false;
        }
    }
    @Test
    @DisplayName("Create failure- not logged in")
    public void createFailLogin(){
        GameData game = new GameData(0, null, null, "Gamename", null);
        try{
            GameData response = serverFacade.createGame(game);
            assert response.gameID() == 0;
            assert response.game() == null;
        }catch(Exception e){
            assert true;
        }
    }
    @Test
    @DisplayName("List Games Success")
    public void listGamesSuccess(){
        UserData registerUser = new UserData("Test", "password", "email@email.com");
        GameData game = new GameData(0, null, null, "Gamename", null);
        GameData gameTwo = new GameData(0, null, null, "DiffGame", null);
        try{
            serverFacade.registerUser(registerUser);
            ListModel response = serverFacade.listGames();
            assert response.games().size() == 0;
            serverFacade.createGame(game);
            serverFacade.createGame(gameTwo);
            ListModel responseTwo = serverFacade.listGames();
            assert responseTwo.games().size() == 2;

        }
        catch(Exception e){
            assert false;
        }
    }
    @Test
    @DisplayName("List Games Fail- Not Logged In")
    public void listGamesFailLoggedOut(){
        UserData registerUser = new UserData("Test", "password", "email@email.com");
        GameData game = new GameData(0, null, null, "game", null);
        try{
            serverFacade.registerUser(registerUser);
            serverFacade.createGame(game);
            serverFacade.logoutUser();
            ListModel response = serverFacade.listGames();
            assert response.games() == null;

        }catch(Exception e){
            assert true;
        }
    }
    @Test
    @DisplayName("Join Game Success")
    public void joinSuccess(){
        UserData registerUser = new UserData("Test", "password", "email@email.com");
        GameData game = new GameData(0, null, null, "game", null);
        try{
            serverFacade.registerUser(registerUser);
            GameData gameData = serverFacade.createGame(game);
            JoinRequest join = new JoinRequest("white", gameData.gameID());
            serverFacade.joinGame(join);
            GameData updatedGame = serverFacade.getGameByID(gameData.gameID());
            assert Objects.equals(updatedGame.whiteUsername(), registerUser.username());


        }catch(Exception e){
            System.out.println("HERE");
            assert false;
        }

    }

    @Test
    @DisplayName("Join Game Fail- Spot Already Taken")
    public void joinFailTaken(){
        UserData firstUser = new UserData("Test", "password", "email@email.com");
        UserData secondUser = new UserData("ShouldnotShow", "password", "email@email.com");
        GameData game = new GameData(0, null, null, "game", null);
        try{
            serverFacade.registerUser(firstUser);
            GameData newGame = serverFacade.createGame(game);
            serverFacade.joinGame(new JoinRequest("white", newGame.gameID() ));
            serverFacade.logoutUser();
            serverFacade.registerUser(secondUser);
            serverFacade.joinGame(new JoinRequest("white", newGame.gameID()));
            assert false;
        }
        catch(Exception e){
            assert true;
        }
    }


}
