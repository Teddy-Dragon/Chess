package server.handlers.services;

import dataaccess.AuthDAO;

import java.util.UUID;

public class CreateAuth {
    //For use in other functions
    private final AuthDAO authMap;

    public CreateAuth(AuthDAO authMap) {
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
