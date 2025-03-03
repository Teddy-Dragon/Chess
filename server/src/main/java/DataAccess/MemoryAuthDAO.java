package DataAccess;

import model.AuthData;

import java.util.HashMap;

public class MemoryAuthDAO{
    private final HashMap<String, AuthData> authMap;

    public MemoryAuthDAO(HashMap<String, AuthData> authMap) {
        this.authMap = authMap;
    }

    public void clearAllAuth(){
        authMap.clear();
        //clear database of authData;
    }
    public AuthData getAuth(String authToken){
        return authMap.get(authToken);
        //Returns authData from database
    }
    public void removeAuth(String authToken){
        AuthData data = authMap.get(authToken);
        authMap.remove(authToken, data);
        //removes one individual's authData
    }
    public void addAuth(String authToken, AuthData authData){
        authMap.put(authToken, authData);
    }
}
