package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessPiece that = (ChessPiece) o;
        return pieceColor == that.pieceColor && type == that.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(pieceColor, type);
    }

    @Override
    public String toString() {
        return "ChessPiece{" +
                "pieceColor=" + pieceColor +
                ", type=" + type +
                '}';
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
         * Bishop: +-N so long as the changes to both col and row are the same absolute value, no one is in the way, and 1 < n < 8
         * Rook: +- N so long as only col or row has that value, the opposing must be 0. No one can be in the way and 1 < n < 8
         * Knight: +-2, +-1 in either category but neither can be the same number. No one can be in the way and the board cannot be over
         * Queen: Same valid moves as both a bishop and a rook, can access their moves instead
         * Pawn: if starting position == current position, +2 row. Otherwise, +1 row if empty and +1 row +1 column if occupied
         * King: +-1 in any direction so long as not occupied nor board ended
         * **/
        if (type == PieceType.QUEEN){
            return queenCalc(board, myPosition);
        }
        if(type == PieceType.BISHOP){
            return bishopCalc(board, myPosition);
        }
        if(type == PieceType.PAWN){
            return pawnCalc(board, myPosition);
        }
        if(type == PieceType.KNIGHT){
            return knightCalc(board, myPosition);
        }
        if(type == PieceType.ROOK){
            return rookCalc(board, myPosition);
        }
        if(type == PieceType.KING){
            return kingCalc(board, myPosition);
        }
        return null;


    }



    public Collection<ChessMove> queenCalc(ChessBoard board, ChessPosition myPosition){
        /* Has same valid moves as both a rook and a bishop */

        Collection<ChessMove> validMoves = new ArrayList<>();
        validMoves.addAll(bishopCalc(board, myPosition));
        validMoves.addAll(rookCalc(board, myPosition));

        return validMoves;

    }

    public Collection<ChessMove> bishopCalc(ChessBoard board, ChessPosition myPosition){
        /* Bishop: +-N so long as the changes to both col and row are the same absolute value, no one is in the way, and 1 <= n + starting POS <= 8 */

        Collection<ChessMove> validMoves = new ArrayList<>();
        int row = myPosition.getRow();
        int col = myPosition.getColumn();
        int change = 1;

        while(row + change <= 8 && col + change <= 8){ /* Check bounds */
            ChessPosition TestPos = new ChessPosition(row + change, col + change);
            if(board.getPiece(TestPos) == null){ /* Check to make sure spot is free */
                validMoves.add(new ChessMove(myPosition, TestPos, null));
                change++;
                continue;
            }
            if (board.getPiece(TestPos).getTeamColor() != pieceColor) { /* if it's not free, is it an enemy? */
                validMoves.add(new ChessMove(myPosition, TestPos, null));
            }

            break;

        }
        change = 1;
        while(row - change >= 1 && col - change >= 1){
            ChessPosition TestPos = new ChessPosition(row - change, col - change);
            if(board.getPiece(TestPos) == null){
                validMoves.add(new ChessMove(myPosition, TestPos, null));
                change++;
                continue;
            }
            if (board.getPiece(TestPos).getTeamColor() != pieceColor) {
                validMoves.add(new ChessMove(myPosition, TestPos, null));
            }
            break;

        }
        change = 1;
        while(row - change >=1 && col + change <= 8){
            ChessPosition TestPos = new ChessPosition(row - change, col + change);
            if(board.getPiece(TestPos) == null){
                validMoves.add(new ChessMove(myPosition, TestPos, null));
                change++;
                continue;
            }
            if (board.getPiece(TestPos).getTeamColor() != pieceColor) {
                validMoves.add(new ChessMove(myPosition, TestPos, null));
            }

            break;

        }
        change = 1;
        while(row + change <= 8 && col - change >= 1){
            ChessPosition TestPos = new ChessPosition(row + change, col - change);
            if(board.getPiece(TestPos) == null){
                validMoves.add(new ChessMove(myPosition, TestPos, null));
                change++;
                continue;
            }
            if (board.getPiece(TestPos).getTeamColor() != pieceColor) {
                validMoves.add(new ChessMove(myPosition, TestPos, null));
            }

            break;

        }

        return validMoves;
    }
    public Collection<ChessMove> pawnCalc(ChessBoard board, ChessPosition myPosition){
        /* Pawn: if starting position == current position, +2 row. Otherwise, +1 row if empty and +1 row +1 column if occupied */
        Collection<ChessMove> validMoves = new ArrayList<>();
        int row = myPosition.getRow();
        int col = myPosition.getColumn();
        int change = 1;
        if(pieceColor == ChessGame.TeamColor.BLACK){
            if(row == 7){
                ChessPosition double_jump = new ChessPosition(row - 2, col);
                ChessPosition jump = new ChessPosition(row - 1, col);
                if(board.getPiece(double_jump) == null && board.getPiece(jump) == null) {
                    validMoves.add(new ChessMove(myPosition, double_jump, null));
                }
            }


            /* Check both diagonals */
            for(int i = 0; i < 2; i++) {
                if(row - 1 >= 1) { /* Check bounds for rows */
                    if (col - change >= 1 && col - change <= 8) { /*Check bounds for columns */
                        ChessPosition occupied = new ChessPosition(row - 1, col - change);
                        if (board.getPiece(occupied) != null) {
                            if (row - 1 == 1) {
                                /* Promote piece */
                                validMoves.add(new ChessMove(myPosition, occupied, PieceType.QUEEN));
                                validMoves.add(new ChessMove(myPosition, occupied, PieceType.ROOK));
                                validMoves.add(new ChessMove(myPosition, occupied, PieceType.KNIGHT));
                                validMoves.add(new ChessMove(myPosition, occupied, PieceType.BISHOP));
                            }  else if (board.getPiece(occupied).getTeamColor() != pieceColor) {
                                validMoves.add(new ChessMove(myPosition, occupied, null));
                            }
                        }
                    }
                }
                change = -change;
            }
            if(row - 1 >= 1) { /* Check bounds */
                ChessPosition jump = new ChessPosition(row - 1, col);
                if (board.getPiece(jump) == null) {
                    if (row - 1 == 1) {
                        /* Promote Piece*/
                        validMoves.add(new ChessMove(myPosition, jump, PieceType.QUEEN));
                        validMoves.add(new ChessMove(myPosition, jump, PieceType.ROOK));
                        validMoves.add(new ChessMove(myPosition, jump, PieceType.KNIGHT));
                        validMoves.add(new ChessMove(myPosition, jump, PieceType.BISHOP));
                    } else {
                        validMoves.add(new ChessMove(myPosition, jump, null));
                    }
                }
            }

        }
        if(pieceColor == ChessGame.TeamColor.WHITE){
            if(row == 2){
                ChessPosition double_jump = new ChessPosition(row + 2, col);
                ChessPosition jump = new ChessPosition(row + 1, col);
                if (board.getPiece(double_jump) == null && board.getPiece(jump) == null) {
                    validMoves.add(new ChessMove(myPosition, double_jump, null));
                }
            }

            /* Check both diagonals */
            for(int i = 0; i < 2; i++) {
                if(row + 1 <= 8) { /* Check bounds for rows */
                    if(col - change >= 1 && col - change <= 8) { /*Check bounds for columns*/
                        ChessPosition occupied = new ChessPosition(row + 1, col - change);
                        if (board.getPiece(occupied) != null) {
                            if (row + 1 == 8) {
                                /* Promote piece */
                                validMoves.add(new ChessMove(myPosition, occupied, PieceType.QUEEN));
                                validMoves.add(new ChessMove(myPosition, occupied, PieceType.ROOK));
                                validMoves.add(new ChessMove(myPosition, occupied, PieceType.KNIGHT));
                                validMoves.add(new ChessMove(myPosition, occupied, PieceType.BISHOP));
                            } else if (board.getPiece(occupied).getTeamColor() != pieceColor) {
                                validMoves.add(new ChessMove(myPosition, occupied, null));
                            }
                        }
                    }
                }
                change = -change;
            }
            if(row + 1 <= 8) { /* Check bounds */
                ChessPosition jump = new ChessPosition(row + 1, col);
                if (board.getPiece(jump) == null) {
                    if (row + 1 == 8) {
                        /*Promote Piece */
                        validMoves.add(new ChessMove(myPosition, jump, PieceType.QUEEN));
                        validMoves.add(new ChessMove(myPosition, jump, PieceType.ROOK));
                        validMoves.add(new ChessMove(myPosition, jump, PieceType.KNIGHT));
                        validMoves.add(new ChessMove(myPosition, jump, PieceType.BISHOP));
                    } else {
                        validMoves.add(new ChessMove(myPosition, jump, null));
                    }

                }
            }




        }

        return validMoves;
    }


    public Collection<ChessMove> rookCalc(ChessBoard board, ChessPosition myPosition){
        /* Rook: +- N so long as only col or row has that value, the opposing must be 0. No one can be in the way and 1 < n + starting POS < 8*/
        Collection<ChessMove> validMoves = new ArrayList<>();
        int row = myPosition.getRow();
        int col = myPosition.getColumn();
        int change = 1;
        while(row + change <= 8){
            ChessPosition TestPos = new ChessPosition(row + change, col);
            if(board.getPiece(TestPos) == null){
                validMoves.add(new ChessMove(myPosition, TestPos, null));
                change++;
                continue;
            }
            if (board.getPiece(TestPos).getTeamColor() != pieceColor) {
                validMoves.add(new ChessMove(myPosition, TestPos, null));
            }
            break;
        }
        change = 1;
        while(row - change >= 1){
            ChessPosition TestPos = new ChessPosition(row - change, col);
            if(board.getPiece(TestPos) == null){
                validMoves.add(new ChessMove(myPosition, TestPos, null));
                change++;
                continue;
            }
            if (board.getPiece(TestPos).getTeamColor() != pieceColor) {
                validMoves.add(new ChessMove(myPosition, TestPos, null));
            }
            break;
        }
        change = 1;
        while(col + change <= 8){
            ChessPosition TestPos = new ChessPosition(row, col + change);
            if(board.getPiece(TestPos) == null){
                validMoves.add(new ChessMove(myPosition, TestPos, null));
                change++;
                continue;
            }
            if (board.getPiece(TestPos).getTeamColor() != pieceColor) {
                validMoves.add(new ChessMove(myPosition, TestPos, null));
            }
            break;
        }
        change = 1;
        while(col - change >= 1) {
            ChessPosition TestPos = new ChessPosition(row, col - change);
            if (board.getPiece(TestPos) == null) {
                validMoves.add(new ChessMove(myPosition, TestPos, null));
                change++;
                continue;
            }
            if (board.getPiece(TestPos).getTeamColor() != pieceColor) {
                validMoves.add(new ChessMove(myPosition, TestPos, null));
            }
            break;
        }
        return validMoves;
    }


    public Collection<ChessMove> knightCalc(ChessBoard board, ChessPosition myPosition){
        Collection<ChessMove> validMoves = new ArrayList<>();
        int row = myPosition.getRow();
        int col = myPosition.getColumn();
        int twoChange = 2;
        int oneChange = 1;
        for(int k = 0; k < 2; k++) {

            for (int i = 0; i < 2; i++) {
                if (row - oneChange <= 8 && row - oneChange >= 1) {
                    if (col - twoChange <= 8 && col - twoChange >= 1) {
                        ChessPosition test = new ChessPosition(row - oneChange, col - twoChange);
                        if (board.getPiece(test) != null) {
                            if (board.getPiece(test).getTeamColor() != pieceColor) {
                                validMoves.add(new ChessMove(myPosition, test, null));
                            }
                        } else {
                            validMoves.add(new ChessMove(myPosition, test, null));
                        }
                    }
                }
                oneChange = -oneChange;
                twoChange = -twoChange;
            }
            for (int i = 0; i < 2; i++) {
                if (row - twoChange <= 8 && row - twoChange >= 1) {
                    if (col - oneChange <= 8 && col - oneChange >= 1) {
                        ChessPosition test = new ChessPosition(row - twoChange, col - oneChange);
                        if (board.getPiece(test) != null) {
                            if (board.getPiece(test).getTeamColor() != pieceColor) {
                                validMoves.add(new ChessMove(myPosition, test, null));
                            }
                        } else {
                            validMoves.add(new ChessMove(myPosition, test, null));
                        }
                    }
                }
                oneChange = -oneChange;
                twoChange = -twoChange;
            }
            oneChange = -oneChange;
        }




        return validMoves;
    }


    public Collection<ChessMove> kingCalc(ChessBoard board, ChessPosition myPosition){
        /* King: +-1 in any direction so long as not occupied nor board ended */
        Collection<ChessMove> validMoves = new ArrayList<>();
        int row = myPosition.getRow();
        int col = myPosition.getColumn();
        int change = 1;

        for(int i = 0; i < 2; i++){ /* up and down both negative and positive */
            if(row - change >= 1 && row - change <= 8){
                ChessPosition testPOS = new ChessPosition(row - change, col);
                if(board.getPiece(testPOS) != null){
                    if(board.getPiece(testPOS).getTeamColor() != pieceColor){
                        validMoves.add(new ChessMove(myPosition,testPOS, null));
                    }
                }
                else {
                    validMoves.add(new ChessMove(myPosition, testPOS, null));
                }
            }
            change = -change;
        }
        for(int i = 0; i < 2; i++){/*Side to side both negative and positive */
            if(col - change <= 8 && col - change >= 1){
                ChessPosition testPOS = new ChessPosition(row, col - change);
                if(board.getPiece(testPOS) != null){
                    if(board.getPiece(testPOS).getTeamColor() != pieceColor){
                        validMoves.add(new ChessMove(myPosition,testPOS, null));
                    }
                }
                else {
                    validMoves.add(new ChessMove(myPosition, testPOS, null));
                }
            }
            change = -change;
        }
        for(int i = 0; i < 2; i++){ /*Diagonal both same sign */
            if(row - change >= 1 && row - change <= 8){
                if(col - change <= 8 && col - change >= 1){
                    ChessPosition testPOS = new ChessPosition(row - change, col - change);
                    if(board.getPiece(testPOS) != null){
                        if(board.getPiece(testPOS).getTeamColor() != pieceColor){
                            validMoves.add(new ChessMove(myPosition,testPOS, null));
                        }
                    }
                    else {
                        validMoves.add(new ChessMove(myPosition, testPOS, null));
                    }

                }
            }
            change = -change;
        }
        for(int i = 0; i < 2; i++){ /*Diagonal different sign */
            if(row - change >= 1 && row - change <= 8){
                if(col + change <= 8 && col + change >= 1){
                    ChessPosition testPOS = new ChessPosition(row - change, col + change);
                    if(board.getPiece(testPOS) != null){
                        if(board.getPiece(testPOS).getTeamColor() != pieceColor){
                            validMoves.add(new ChessMove(myPosition,testPOS, null));
                        }
                    }
                    else {
                        validMoves.add(new ChessMove(myPosition, testPOS, null));
                    }

                }
            }
            change = -change;
        }



        return validMoves;
    }




}
