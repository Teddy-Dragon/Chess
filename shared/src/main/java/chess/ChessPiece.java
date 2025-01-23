package chess;

import java.util.Collection;
import java.util.List;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {

    private final ChessGame.TeamColor pieceColor;
    private final PieceType type;

    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        this.pieceColor = pieceColor;
        this.type = type;
    }

    /**
     * The various different chess piece options
     */
    public enum PieceType {
        KING,
        QUEEN,
        BISHOP,
        KNIGHT,
        ROOK,
        PAWN
    }

    /**
     * @return Which team this chess piece belongs to
     */
    public ChessGame.TeamColor getTeamColor() {
        return pieceColor;
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {
        return type;
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        /** When given PieceType, calculate valid moves based off current position of Self and current position of Others on board
         * Bishop: +-N so long as the changes to both col and row are the same value, no one is in the way, and 1 < n < 8
         * Rook: +- N so long as only col or row has that value, the opposing must be 0. No one can be in the way and 1 < n < 8
         * Knight: +-2, +-1 in either category but neither can be the same number. No one can be in the way and the board cannot be over
         * Queen: Same valid moves as both a bishop and a rook, can access their moves instead
         * Pawn: if starting position == current position, +2 row. Otherwise, +1 row if empty and +1 row +1 column if occupied
         * King: +-1 in any direction so long as not occupied nor board ended
         * **/
        if (type == PieceType.QUEEN){
            return QueenCalc(board, myPosition);
        }
        if(type == PieceType.BISHOP){
            return BishopCalc(board, myPosition);
        }
        return null;


    }

    public Collection<ChessMove> QueenCalc(ChessBoard board, ChessPosition myPosition){
        return null;

    }
    public Collection<ChessMove>BishopCalc(ChessBoard board, ChessPosition myPosition){
        Collection<ChessMove> validMoves;

        System.out.println(myPosition.getRow() + "  " + myPosition.getColumn());

        return null;
    }




}
