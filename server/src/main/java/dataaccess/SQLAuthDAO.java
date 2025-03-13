package dataaccess;

import model.AuthData;

import java.util.UUID;

public class SQLAuthDAO implements AuthDAO{
    public SQLAuthDAO() throws Exception{
        String[] createStatements = {
                """
            CREATE TABLE IF NOT EXISTS auth(
            `authToken` varchar(256) NOT NULL,
            `userName` varchar(256) NOT NULL,
            PRIMARY KEY (`authToken`),
            INDEX(username)
            )


            """

        };
        new DatabaseManager().configureDatabase(createStatements);
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

}
