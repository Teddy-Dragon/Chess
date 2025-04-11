package client;

import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPosition;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ui.ChessBoardDisplay;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ChessboardDisplayTests {
    ChessGame starting;
    @BeforeEach
    public void setup(){
        starting = new ChessGame();
    }
    @Test
    @DisplayName("Prints alternative boards")
    public void printChessBoard(){
        ChessGame.TeamColor playerColor = ChessGame.TeamColor.WHITE;
        String whiteMovesPawn = new TestBoards().whiteMovesPawn();
        String chessBoardDisplay1 = new ChessBoardDisplay().chessBoardDisplay(playerColor, starting, null);
        ChessPosition chessPosition = new ChessPosition(2, 1);
        System.out.println(starting.validMoves(chessPosition));
        ChessMove move = new ChessMove(new ChessPosition(2, 1), new ChessPosition(3, 1),null);
        try{
            starting.makeMove(move);}
        catch (Exception e){
            assert false;
        }
        String chessBoardDisplay2 = new ChessBoardDisplay().chessBoardDisplay(playerColor, starting, null);
        assert !Objects.equals(chessBoardDisplay1, chessBoardDisplay2);
        assert Objects.equals(chessBoardDisplay2, whiteMovesPawn);
    }
    @Test
    @DisplayName("Making sure highlighting works- Starting")
    public void printChessBoardHighLightsStart(){
        ChessGame.TeamColor playerColor = ChessGame.TeamColor.WHITE;
        List<ChessMove> validMoves = (List<ChessMove>) starting.validMoves(new ChessPosition(2, 1));
        List<ChessPosition> validPositions = new ArrayList<>();
        for(ChessMove move : validMoves){
            validPositions.add(move.getEndPosition());
        }
        String chessBoardDisplay1 = new ChessBoardDisplay().chessBoardDisplay(playerColor, starting, validPositions);
        assert Objects.equals(chessBoardDisplay1, new TestBoards().highlightA2());

        List<ChessMove> validMovesBlack = (List<ChessMove>) starting.validMoves(new ChessPosition(7, 8));
        List<ChessPosition> validPositionsBlack = new ArrayList<>();
        for(ChessMove move: validMovesBlack){
            validPositionsBlack.add(move.getEndPosition());
        }
        String chessBoardDisplay2 = new ChessBoardDisplay().chessBoardDisplay(playerColor, starting, validPositionsBlack);
        assert Objects.equals(chessBoardDisplay2, new TestBoards().highlightH7());
    }
    @Test
    @DisplayName("If the pieces are not in the starting positions- will it highlight?")
    public void printChessBoardHighlightsAdvanced() {
        ChessMove move = new ChessMove(new ChessPosition(1, 2), new ChessPosition(3, 1), null);
        ChessMove move2 = new ChessMove(new ChessPosition(7, 6), new ChessPosition(5, 6), null);
        try{
            starting.makeMove(move);
            starting.makeMove(move2);
        }catch (Exception e){
            assert false;
        }
        List<ChessMove> validMoves = (List<ChessMove>) starting.validMoves(new ChessPosition(3, 1));
        List<ChessPosition> validPositions = new ArrayList<>();
        for(ChessMove chessMove: validMoves){
            validPositions.add(chessMove.getEndPosition());
        }
        String chessBoardDisplay = new ChessBoardDisplay().chessBoardDisplay(ChessGame.TeamColor.WHITE, starting, validPositions);
        System.out.println(chessBoardDisplay);
    }
}
