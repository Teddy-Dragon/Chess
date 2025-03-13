package dataaccess;

import model.AuthData;

import java.util.UUID;

public interface AuthDAO {
    public void clearAllAuth();
    public AuthData getAuth(UUID authToken);
    public void removeAuth(UUID authToken);
    public void addAuth(UUID authToken, AuthData authData);
}
