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
}
