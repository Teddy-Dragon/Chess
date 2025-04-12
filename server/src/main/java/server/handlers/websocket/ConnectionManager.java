package server.handlers.websocket;

import chess.ChessGame;
import chess.ChessMove;
import model.GameData;
import org.eclipse.jetty.websocket.api.Session;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

public class ConnectionManager {
        public final ConcurrentHashMap<String, Connection> connections = new ConcurrentHashMap<>();
        public ConcurrentHashMap<Integer,Lobby> lobbies = new ConcurrentHashMap<>();

        public void add(String visitorName, Session session, int gameID, GameData gameData) throws Exception {
            var connection = new Connection(visitorName, session);
            connections.put(visitorName, connection);
            if(lobbies.get(gameID) == null){
                lobbies.put(gameID, new Lobby());
            }
            lobbies.get(gameID).setConnections(visitorName, connection);
            String joiningAs = "";
            if(Objects.equals(gameData.whiteUsername(), visitorName)){
                joiningAs = " and is playing as White!";
            }
            if(Objects.equals(gameData.blackUsername(), visitorName)){
                joiningAs = " and is playing as Black";
            }
            else{
                joiningAs = " as an Observer!";
            }
            String message = visitorName + " has joined" + joiningAs;
            broadcastLobby(visitorName, message, gameID);
        }

        public void remove(String visitorName, int gameID) throws Exception {
            connections.remove(visitorName);
            lobbies.get(gameID).removeConnection(visitorName);
            String message = visitorName + " has left";
            broadcastLobby(visitorName, message, gameID);
        }
        public void gameUpdate(String username, int gameID, ChessGame game, ChessMove move) throws Exception {
            List<Connection> disconnectedUsers = new ArrayList<>();
            ConcurrentHashMap<String, Connection>  lobby = lobbies.get(gameID).getConnections();
            String message = username + " moved " + move.getStartPosition() + " to " + move.getEndPosition();
            for(Connection player: lobby.values()){
                if(player.session.isOpen()){
                    if(!Objects.equals(username, player.username)){
                        player.update(game);
                        player.send(message);
                    }
                }else{
                    disconnectedUsers.add(player);
                }
            }
            for(var disconnected: disconnectedUsers){
                connections.remove(disconnected.username);
            }
        }
        public void broadcastLobby(String noBrodcastName, String message, int gameID) throws Exception {
            List<Connection> disconnectedUsers = new ArrayList<>();
            ConcurrentHashMap<String, Connection> lobby = lobbies.get(gameID).getConnections();
            for(var player: lobby.values()){
                if(player.session.isOpen()){
                    if(!Objects.equals(player.username, noBrodcastName)){
                        player.send(message);
                    }
                }
                else{
                    disconnectedUsers.add(player);
                }
            }
            for(var disconnected: disconnectedUsers){
                connections.remove(disconnected.username);
            }
        }
}
