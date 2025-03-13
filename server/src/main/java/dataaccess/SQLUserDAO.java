package dataaccess;

import model.UserData;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.SQLException;

public class SQLUserDAO implements UserDAO{
    public SQLUserDAO() throws Exception{
        String[] createStatements = {
                """
            CREATE TABLE IF NOT EXISTS users(
            `username` varchar(256) NOT NULL,
            `password` varchar(256) NOT NULL,
            `email` varchar(256) NOT NULL,
             PRIMARY KEY(`username`),
             INDEX(password),
             INDEX(email)
             )
            
            """
        };
        new DatabaseManager().configureDatabase(createStatements);
    }
    public void clearAllUsers() {
        String deleteStatements = "TRUNCATE TABLE users";
        try(var conn = DatabaseManager.getConnection()){
            try(var preparedStatement = conn.prepareStatement(deleteStatements)){
                preparedStatement.executeUpdate();
            }
        }catch(Exception ignored){

        }

    }

    public void addUser(UserData userData) {
        String statement = "INSERT INTO users(username, password, email) VALUES (?, ?, ?)";
        try(var conn = DatabaseManager.getConnection()){

            try(var preparedStatement = conn.prepareStatement(statement)){
                preparedStatement.setString(1, userData.username());
                preparedStatement.setString(2, BCrypt.hashpw(userData.password(), BCrypt.gensalt()));
                preparedStatement.setString(3, userData.email());
                preparedStatement.executeUpdate();
            }catch (SQLException exception){
                System.out.println(exception);
            }
        }catch(Exception e){
            System.out.println(e);
        }
    }

    public UserData getUser(String username) {
        String statement = "SELECT username, password, email FROM users WHERE username=?";
        try(var conn = DatabaseManager.getConnection()){
            try(var preparedStatement = conn.prepareStatement(statement)){
                preparedStatement.setString(1, username);
                try(var data = preparedStatement.executeQuery()){
                    if(data.next()){
                        return new UserData(data.getString("username"), data.getString("password"), data.getString("email"));
                    }
                }
            }
        }catch(Exception e){
            System.out.println(e);
        }
        return null;

    }

}
