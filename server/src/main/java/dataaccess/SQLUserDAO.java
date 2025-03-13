package dataaccess;

import model.UserData;

import java.sql.SQLException;

public class SQLUserDAO implements UserDAO{
    public SQLUserDAO() throws Exception{
        configureDatabase();
    }
    public void clearAllUsers() {
        String deleteStatements = "DROP TABLE IF EXISTS users";
        try(var conn = DatabaseManager.getConnection()){
            try(var preparedStatement = conn.prepareStatement(deleteStatements)){
                preparedStatement.executeUpdate();
            }
        }catch(Exception ignored){

        }

    }

    public void addUser(String username, UserData userData) {

    }

    public UserData getUser(String username) {
        return null;
    }

    private final String[] createStatements = {
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
    private void configureDatabase() throws Exception {
        DatabaseManager.createDatabase();
        try(var conn = DatabaseManager.getConnection()){
            for(var statement : createStatements){
                try(var preparedStatement = conn.prepareStatement(statement)){
                    preparedStatement.executeUpdate();
                }
            }
        }catch(SQLException e){
            throw new Exception(e);
        }

    }
}
