package dataaccess;

import chess.ChessGame;
import model.GameData;
import org.junit.jupiter.api.*;

import java.util.List;

public class GameDAOTest {

    static GameDAO gameMap;

    @BeforeAll
    public static void init() {
        try{
            gameMap = new SQLGameDAO();
        }catch(Exception e){

        }

    }
    @AfterEach
    public void cleanUp(){
        gameMap.clearAllGames();
    }

    @Test
    @DisplayName("Create Game Success")
    public void createSucces(){
        GameData testGame = new GameData(123, null, null, "Games", new ChessGame());
        gameMap.addGame(testGame);
        Assertions.assertEquals(gameMap.getGameByID(testGame.gameID()).gameName(), testGame.gameName());

    }
    @Test
    @DisplayName("Creation Failed")
    public void createFail(){
        try{
            GameData testGame = new GameData(1, null, null, null, null);
            gameMap.addGame(testGame);
        }
        catch (Exception e){
            assert true;
        }
    }

    @Test
    @DisplayName("Update Game")
    public void updateSuccess(){
        GameData testGame = new GameData(123, null, null, "name", new ChessGame());
        gameMap.addGame(testGame);
        GameData newGame = new GameData(123, "player_one", null, "Alternate Name", new ChessGame());
        gameMap.updateGame(testGame.gameID(), newGame);
        Assertions.assertEquals(newGame.whiteUsername(), gameMap.getGameByID(testGame.gameID()).whiteUsername());

    }
    @Test
    @DisplayName("Update Game Fail")
    public void updateFail(){
        GameData newGame = new GameData(123, "player_one", null, "Alternate Name", new ChessGame());
        gameMap.updateGame(000, newGame);
        Assertions.assertEquals(gameMap.getGameByID(000), null);
    }

    @Test
    @DisplayName("Clear Games")
    public void clearSuccess(){
        GameData testGame = new GameData(123, null, null, "name", new ChessGame());
        gameMap.addGame(testGame);
        assert gameMap.getGameByID(testGame.gameID()) != null;
        gameMap.clearAllGames();
        GameData response = gameMap.getGameByID(testGame.gameID());
        assert response == null;

    }
    @Test
    @DisplayName("List Games Success")
    public void listGamesSuccess(){
        GameData firstGame = new GameData(123, null, null, "name", new ChessGame());
        GameData secondGame = new GameData(99323, null, null, "another", new ChessGame());
        GameData thirdGame = new GameData(2351135, null, null, "name", new ChessGame());
        gameMap.addGame(firstGame);
        gameMap.addGame(secondGame);
        gameMap.addGame(thirdGame);
        List<GameData> listOfGames = gameMap.getAllGames();
        Assertions.assertEquals(3, listOfGames.size());
    }
    @Test
    @DisplayName("Get Game Success")
    public void getGameSuccess(){
        GameData firstGame = new GameData(123, null, null, "name", new ChessGame());
        GameData secondGame = new GameData(99323, "Player", null, "another", new ChessGame());
        GameData thirdGame = new GameData(2351135, null, null, "name", new ChessGame());
        gameMap.addGame(firstGame);
        gameMap.addGame(secondGame);
        gameMap.addGame(thirdGame);
        Assertions.assertEquals(firstGame.gameName(), gameMap.getGameByID(firstGame.gameID()).gameName());
        Assertions.assertEquals(secondGame.whiteUsername(), gameMap.getGameByID(secondGame.gameID()).whiteUsername());
        Assertions.assertEquals(thirdGame.gameName(), gameMap.getGameByID(thirdGame.gameID()).gameName());

    }

    @Test
    @DisplayName("Get Game Fail")
    public void getGameFail(){
        try{
            assert gameMap.getGameByID(-5543) == null;

        }
        catch(Exception e){
            assert false;

        }

    }
}
