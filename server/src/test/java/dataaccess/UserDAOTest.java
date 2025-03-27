package dataaccess;

import model.UserData;
import org.junit.jupiter.api.*;
import org.mindrot.jbcrypt.BCrypt;

public class UserDAOTest {
    private static UserDAO userMap;

    @BeforeAll
    public static void init() {
        try{
            userMap = new SQLUserDAO();
        }catch(Exception e){

        }

    }
    @AfterEach
    public void cleanUp(){
        userMap.clearAllUsers();
    }

    @Test
    @DisplayName("Adding A User")
    public void addUserSuccess(){
        UserData testUser = new UserData("username", "password", "email@email.com");
        userMap.addUser(testUser);
        UserData answer = userMap.getUser("username");

        Assertions.assertEquals(testUser.username(), answer.username());
        Assertions.assertEquals(testUser.email(), answer.email());
        assert BCrypt.checkpw(testUser.password(), answer.password());


    }

    @Test
    @DisplayName("Failure To Add User")
    public void addUserFail(){

        try{
            UserData testUser = new UserData("DOESNOTEXIST", null, null);
            userMap.addUser(testUser);
        }catch(Exception e){
            assert true;
        }
        assert false;
    }

    @Test
    @DisplayName("Get User")
    public void getUserSuccess(){
        UserData testUser = new UserData("username", "password", "email@email.com");
        userMap.addUser(testUser);
        UserData response = userMap.getUser(testUser.username());
        Assertions.assertEquals(testUser.username(), response.username());
        Assertions.assertEquals(testUser.email(), response.email());
        assert BCrypt.checkpw(testUser.password(), response.password());
    }
    @Test
    @DisplayName("Get User Failure")
    public void getUserFail(){
        UserData response = userMap.getUser("DOESNOTEXIST");
        assert response == null;
    }

    @Test
    @DisplayName("Clear Users")
    public void clearSuccess(){
        UserData testUser = new UserData("username", "password", "email@email.com");
        userMap.addUser(testUser);
        userMap.clearAllUsers();
        assert userMap.getUser(testUser.username()) == null;

    }


}
