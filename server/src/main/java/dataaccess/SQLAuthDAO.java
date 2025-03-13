package dataaccess;

import model.AuthData;

import java.sql.SQLException;
import java.util.UUID;

public class SQLAuthDAO implements AuthDAO{
    public SQLAuthDAO() throws Exception{
        configureDatabase();
    }

    @Override
    public void clearAllAuth() {
        String deleteStatements = "DROP TABLE IF EXISTS auth";
        try(var conn = DatabaseManager.getConnection()){
            try(var preparedStatement = conn.prepareStatement(deleteStatements)){
                preparedStatement.executeUpdate();
            }
        }catch(Exception ignored){

        }


    }
    @Override
    public AuthData getAuth(UUID authToken) {
        return null;
    }
    @Override
    public void removeAuth(UUID authToken) {

    }
    @Override
    public void addAuth(UUID authToken, AuthData authData) {

    }

    private final String[] createStatements= {
            """
            CREATE TABLE IF NOT EXISTS auth(
            `authToken` varchar(256) NOT NULL,
            `userName` varchar(256) NOT NULL,
            PRIMARY KEY (`authToken`),
            INDEX(username)
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
