package dataaccess;

import model.AuthData;

import java.util.UUID;

public class SQLAuthDAO implements AuthDAO{
    public SQLAuthDAO(){
    }


    public void clearAllAuth() {

    }

    public AuthData getAuth(UUID authToken) {
        return null;
    }

    public void removeAuth(UUID authToken) {

    }

    public void addAuth(UUID authToken, AuthData authData) {

    }

    private final String[] createStatements= {
            """
            CREATE TABLE IF NOT EXISTS auth(
            'authToken' varchar(256) NOT NULL,
            'username' varchar(256) NOT NULL,
            PRIMARY KEY ('authToken'),
            INDEX(username)
            )


            """

    };
}
