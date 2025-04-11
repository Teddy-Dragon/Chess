package server.handlers.websocket;

import java.util.concurrent.ConcurrentHashMap;

public class Lobby {
    ConcurrentHashMap<String, Connection> connections = new ConcurrentHashMap<>();

    public void setConnections(String username, Connection connection){
        connections.put(username, connection);
    }
    public ConcurrentHashMap<String, Connection> getConnections(){
        return connections;
    }
    public void removeConnection(String username){
        connections.remove(username);
    }
}
