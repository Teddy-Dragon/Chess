package chess;

import java.util.Collection;
import java.util.ArrayList;
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

    public ChessPiece(ChessGame.TeamColor pieceColor, PieceType type) {
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
        if(type == PieceType.KING){
            return kingCalc(board, myPosition);
        }
        if(type == PieceType.QUEEN){
            return queenCalc(board, myPosition);
        }
        if(type == PieceType.BISHOP){
            return bishopCalc(board, myPosition);
        }
        if(type == PieceType.KNIGHT){
            return knightCalc(board, myPosition);
        }
        if(type == PieceType.ROOK){
            return rookCalc(board, myPosition);
        }
        if (type == PieceType.PAWN){
            return pawnCalc(board, myPosition);
        }
        return null;
    }

    public Collection<ChessMove> kingCalc(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> validMoves = new ArrayList<>();
        int row = myPosition.getRow();
        int col = myPosition.getColumn();
        int change = 1;

        for(int i = 0; i < 2; i++){ /*Checking side to side and forward and back */
            if(row - change <=8 && row - change >=1){
                ChessPosition Forward = new ChessPosition(row - change, col);
                if(board.getPiece(Forward) == null || board.getPiece(Forward).getTeamColor() != pieceColor){
                    validMoves.add(new ChessMove(myPosition, Forward, null));
                }
            }
            if(col - change <=8 && col - change >=1){
                ChessPosition Forward = new ChessPosition(row, col - change);
                if(board.getPiece(Forward) == null || board.getPiece(Forward).getTeamColor() != pieceColor){
                    validMoves.add(new ChessMove(myPosition, Forward, null));
                }
            }
            change = -change;
        }
        for(int i  = 0; i < 2; i++){ /*Checking Diagonals */
            if(row - change <=8 && row - change >= 1){
                if(col - change <=8 && col - change >= 1){
                    ChessPosition Diagonal = new ChessPosition(row - change, col - change);
                    if(board.getPiece(Diagonal) == null || board.getPiece(Diagonal).getTeamColor() != pieceColor){
                        validMoves.add(new ChessMove(myPosition, Diagonal, null));
                    }
                }
            }
            if(row - change <=8 && row - change >= 1){
                if(col + change <=8 && col + change >= 1){
                    ChessPosition Diagonal = new ChessPosition(row - change, col + change);
                    if(board.getPiece(Diagonal) == null || board.getPiece(Diagonal).getTeamColor() != pieceColor){
                        validMoves.add(new ChessMove(myPosition, Diagonal, null));
                    }
                }
            }
            change = -change;
        }




        return validMoves;
    }
    public Collection<ChessMove> queenCalc(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> validMoves = new ArrayList<>();
        validMoves.addAll(bishopCalc(board, myPosition));
        validMoves.addAll(rookCalc(board, myPosition));

        return validMoves;
    }
    public Collection<ChessMove> bishopCalc(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> validMoves = new ArrayList<>();
        int row = myPosition.getRow();
        int col = myPosition.getColumn();
        int change = 1;
        int sign = 1;
        for(int i = 0; i < 2; i++){ /*Diagonal Both signs are the same */
            while(row - change <=8 && row - change >=1){
                if (col - change <=8 && col - change >=1){
                    ChessPosition test = new ChessPosition(row - change, col - change);
                    if(board.getPiece(test) != null){
                        if(board.getPiece(test).getTeamColor() != pieceColor){
                            validMoves.add(new ChessMove(myPosition, test, null));
                            break;
                        }
                        break;
                    }
                    else{
                        validMoves.add(new ChessMove(myPosition, test, null));
                        change = change + sign;
                    }
                }else{
                    break;}
            }
            change = -1;
            sign = -1;
        }
        change = 1;
        sign = 1;

        for(int i = 0; i < 2; i++){ /* Diagonal Different Signs */
            while(row - change <=8 && row - change >=1){
                if (col + change <=8 && col + change >=1){
                    ChessPosition test = new ChessPosition(row - change, col + change);
                    if(board.getPiece(test) != null){
                        if(board.getPiece(test).getTeamColor() != pieceColor){
                            validMoves.add(new ChessMove(myPosition, test, null));
                            break;
                        }
                        break;
                    }
                    else{
                        validMoves.add(new ChessMove(myPosition, test, null));
                        change = change + sign;
                    }
                }else{
                    break;}
            }
            change = -1;
            sign = -1;
        }

        return validMoves;
    }
    public Collection<ChessMove> knightCalc(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> validMoves = new ArrayList<>();
        /*+-2 & +- 1 so long as its one or the other */
        int row = myPosition.getRow();
        int col = myPosition.getColumn();
        int twoChange = 2;
        int oneChange = 1;
        for(int k = 0; k < 2; k++){
            for(int i = 0; i < 2; i++){
                if(row - twoChange <=8 && row - twoChange >=1){
                    if(col - oneChange <=8 && col - oneChange >=1){
                        ChessPosition test = new ChessPosition(row - twoChange, col - oneChange);
                        if(board.getPiece(test) == null || board.getPiece(test).getTeamColor() != pieceColor){
                            validMoves.add(new ChessMove(myPosition, test, null));
                        }
                    }
                }
                if(row - oneChange <=8 && row - oneChange >=1){
                    if(col - twoChange <=8 && col - twoChange >= 1){
                        ChessPosition test = new ChessPosition(row - oneChange, col - twoChange);
                        if(board.getPiece(test) == null || board.getPiece(test).getTeamColor() != pieceColor){
                            validMoves.add(new ChessMove(myPosition, test, null));
                        }
                    }
                }
                oneChange = -oneChange;
                twoChange = - twoChange;

            }
            oneChange = -oneChange;
        }





        return validMoves;
    }
    public Collection<ChessMove> rookCalc(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> validMoves = new ArrayList<>();
        int row = myPosition.getRow();
        int col = myPosition.getColumn();
        int change = 1;
        int sign = 1;
        for(int i = 0; i < 2; i++) {
            while (row - change <= 8 && row - change >= 1) {
                ChessPosition test = new ChessPosition(row - change, col);
                if (board.getPiece(test) != null) { /*if the spot is not available*/
                    if (board.getPiece(test).getTeamColor() != pieceColor) { /* If it is an enemy */
                        validMoves.add(new ChessMove(myPosition, test, null));
                        break;
                    }
                    break;
                } else {
                    validMoves.add(new ChessMove(myPosition, test, null));
                    change = change + sign;
                }
            }
            change = -1;
            sign = -1;

        }
        change = 1;
        sign = 1;
        for(int i = 0; i < 2; i++) {
            while (col - change <= 8 && col - change >= 1) {
                ChessPosition test = new ChessPosition(row, col - change);
                if (board.getPiece(test) != null) { /*if the spot is not available*/
                    if (board.getPiece(test).getTeamColor() != pieceColor) { /* If it is an enemy */
                        validMoves.add(new ChessMove(myPosition, test, null));
                        break;
                    }
                    break;
                } else {
                    validMoves.add(new ChessMove(myPosition, test, null));
                    change = change + sign;
                }
            }
            change = -1;
            sign = -1;
        }

        return validMoves;
    }

    public void pawnPromotion(int row, Collection<ChessMove> validMoves, ChessPosition myPosition, ChessPosition newPosition){
        if(pieceColor == ChessGame.TeamColor.WHITE){
            if(row + 1 == 8){
                validMoves.add(new ChessMove(myPosition, newPosition, PieceType.QUEEN));
                validMoves.add(new ChessMove(myPosition, newPosition, PieceType.ROOK));
                validMoves.add(new ChessMove(myPosition, newPosition, PieceType.KNIGHT));
                validMoves.add(new ChessMove(myPosition, newPosition, PieceType.BISHOP));

            }
            else{
                validMoves.add(new ChessMove(myPosition, newPosition, null));
            }
        }
        if(pieceColor == ChessGame.TeamColor.BLACK){
            if(row - 1 == 1){ /*if the capture spot diagonal to the pawn results in promotion */
                validMoves.add(new ChessMove(myPosition, newPosition, PieceType.QUEEN));
                validMoves.add(new ChessMove(myPosition, newPosition, PieceType.ROOK));
                validMoves.add(new ChessMove(myPosition, newPosition, PieceType.KNIGHT));
                validMoves.add(new ChessMove(myPosition, newPosition, PieceType.BISHOP));
            }
            else{ /*Normal Capture */
                validMoves.add(new ChessMove(myPosition, newPosition, null));
            }

        }

    }
    public Collection<ChessMove> pawnCalc(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> validMoves = new ArrayList<>();
        int row = myPosition.getRow();
        int col = myPosition.getColumn();
        int change = 1;

        if(pieceColor == ChessGame.TeamColor.WHITE){
            if(row == 2){
                ChessPosition doubleJump = new ChessPosition(row + 2, col);
                ChessPosition jump = new ChessPosition(row + 1, col);
                if(board.getPiece(doubleJump) == null && board.getPiece(jump) == null){
                    validMoves.add(new ChessMove(myPosition, doubleJump, null));
                }
            }

            for(int i = 0; i < 2; i++){
                if(row + 1 <= 8){
                    if(col - change <=8 && col - change >=1){
                        ChessPosition capture = new ChessPosition(row + 1, col - change);
                        if(board.getPiece(capture) != null && board.getPiece(capture).getTeamColor() != pieceColor){
                            pawnPromotion(row, validMoves, myPosition, capture);
                        }
                    }
                }
                change = -change;
            }
            if(row + 1 <= 8){
                ChessPosition jump = new ChessPosition(row + 1, col);
                if(board.getPiece(jump) == null){
                    pawnPromotion(row, validMoves, myPosition, jump);
                }
            }


        }


        if(pieceColor == ChessGame.TeamColor.BLACK){
            if(row == 7){
                ChessPosition doubleJump = new ChessPosition(row - 2, col);
                ChessPosition jump = new ChessPosition(row - 1, col);
                if(board.getPiece(doubleJump) == null && board.getPiece(jump) == null){
                    validMoves.add(new ChessMove(myPosition, doubleJump, null));
                }
            }
            for(int i = 0; i < 2; i++){
                if(row - 1 >= 1){
                    if(col - change <=8 && col - change >=1){
                        ChessPosition capture = new ChessPosition(row - 1, col - change);
                        if(board.getPiece(capture) != null && board.getPiece(capture).getTeamColor() != pieceColor){
                           pawnPromotion(row, validMoves, myPosition, capture);
                        }
                    }
                }
                change = -change;
            }
            if(row - 1 >= 1){
                ChessPosition jump = new ChessPosition(row - 1, col);
                if(board.getPiece(jump) == null){
                    pawnPromotion(row, validMoves, myPosition, jump);
                }
            }

        }

        return validMoves;
    }

}
