package dataaccess;

import model.AuthData;

import java.util.HashMap;
import java.util.UUID;

public class MemoryAuthDAO{
    private final HashMap<UUID, AuthData> authMap;

    public MemoryAuthDAO(HashMap<UUID, AuthData> authMap) {
        this.authMap = authMap;
    }

    public void clearAllAuth(){
        authMap.clear();
        //clear database of authData;
    }
    public AuthData getAuth(UUID authToken){
        return authMap.get(authToken);
        //Returns authData from database
    }
    public void removeAuth(UUID authToken){
        AuthData data = authMap.get(authToken);
        authMap.remove(authToken, data);
        //removes one individual's authData
    }
    public void addAuth(UUID authToken, AuthData authData){
        authMap.put(authToken, authData);
    }
}
