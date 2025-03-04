package DataAccess;

import model.AuthData;

public interface AuthDAO {
    public void clearAllAuth();
    public AuthData getAuth();
    public void removeAuth();
    public void addAuth(AuthData authData);
}
