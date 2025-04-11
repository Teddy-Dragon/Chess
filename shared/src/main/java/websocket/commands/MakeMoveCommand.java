package websocket.commands;

import chess.ChessMove;

public class MakeMoveCommand{
    private final CommandType commandType;

    private final String authToken;

    private final Integer gameID;
    private final ChessMove move;
    public MakeMoveCommand(CommandType command, String authToken, Integer gameID, ChessMove move) {
        this.commandType = command;
        this.authToken = authToken;
        this.gameID = gameID;
        this.move = move;
    }
    public enum CommandType {
        MAKE_MOVE,

    }

    public CommandType getCommandType() {
        return commandType;
    }

    public String getAuthToken() {
        return authToken;
    }

    public Integer getGameID() {
        return gameID;
    }

    public ChessMove getMove(){return move;}

}
