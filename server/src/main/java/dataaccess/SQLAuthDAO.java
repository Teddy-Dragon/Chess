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
        String deleteStatements = "TRUNCATE TABLE auth";
        try(var conn = DatabaseManager.getConnection()){
            try(var preparedStatement = conn.prepareStatement(deleteStatements)){
                preparedStatement.executeUpdate();
            }
        }catch(Exception ignored){

        }


    }
    @Override
    public AuthData getAuth(UUID authToken) {
        String uuid = String.valueOf(authToken);
        String statement = "SELECT authToken, userName FROM auth WHERE authToken=?";
        try(var conn = DatabaseManager.getConnection()){
            try(var preparedStatement = conn.prepareStatement(statement)){
                preparedStatement.setString(1, uuid);
                try(var data = preparedStatement.executeQuery()){
                    if(data.next()){
                        UUID responseToken = UUID.fromString(data.getString("authToken"));
                        return new AuthData(responseToken, data.getString("userName"));
                    }
                }
            }
        }
        catch(Exception e){
            System.out.println(e);
            return null;
        }

        return null;
    }
    @Override
    public void removeAuth(UUID authToken) {
        String statement = "DELETE FROM auth WHERE authToken=?";
        try(var conn = DatabaseManager.getConnection()){
            try(var prepareStatement = conn.prepareStatement(statement)){
                prepareStatement.setString(1, String.valueOf(authToken));
                prepareStatement.executeUpdate();
            }
        }catch(Exception e){
            System.out.println(e);
        }

    }
    @Override
    public void addAuth(AuthData authData) {
        String statement = "INSERT INTO auth(authToken, userName) VALUES(?,?)";

        try(var conn = DatabaseManager.getConnection()){
            if(authData.authToken() == null){
                throw new Exception();
            }
            try(var prepareStatement = conn.prepareStatement(statement)){
                prepareStatement.setString(1, String.valueOf(authData.authToken()));
                prepareStatement.setString(2, authData.username());
                prepareStatement.executeUpdate();
            }
        }catch (Exception e){
            System.out.println(e);
        }

    }

}
