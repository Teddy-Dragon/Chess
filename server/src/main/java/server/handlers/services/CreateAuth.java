package server.handlers.services;

import java.util.UUID;

public class CreateAuth {
    //For use in other functions
    public UUID newToken(){
        UUID authToken = new UUID(12, 8);
        return authToken;
    }

}
