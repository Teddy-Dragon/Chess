package server.handlers.services;

import DataAccess.MemoryAuthDAO;

import java.util.UUID;

public class CreateAuth {
    //For use in other functions
    private final MemoryAuthDAO authMap;

    public CreateAuth(MemoryAuthDAO authMap) {
        this.authMap = authMap;
    }

    public UUID newToken(){
        UUID newUUID = UUID.randomUUID();
        while(authMap.getAuth(newUUID) != null){
            newUUID = UUID.randomUUID();
        }
        return newUUID;
    }

}
