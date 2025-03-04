package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {
    private ChessBoard gameBoard = new ChessBoard();
    private TeamColor currentPlayer = TeamColor.WHITE;

    public ChessGame() {
        gameBoard.resetBoard();

    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return currentPlayer;
    }

    /**
     * Set's which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {

        currentPlayer = team;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessGame chessGame = (ChessGame) o;
        return Objects.equals(gameBoard, chessGame.gameBoard) && currentPlayer == chessGame.currentPlayer;
    }

    @Override
    public int hashCode() {
        return Objects.hash(gameBoard, currentPlayer);
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    /**
     * Gets a valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        Collection<ChessMove> validMoves = new ArrayList<>();
        ChessPiece piece = gameBoard.getPiece(startPosition);
        Collection<ChessMove> possibleMoves = piece.pieceMoves(gameBoard, startPosition);
        if(gameBoard.getPiece(startPosition) == null){
            return null;
        }
        for(ChessMove move: possibleMoves){ /*For all moves check to see if it puts the king in check */
            if(isAValidMove(move)){
                validMoves.add(move);
            }

        }



        return validMoves;
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to preform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        ChessPiece piece = gameBoard.getPiece(move.getStartPosition());


        if(move.getPromotionPiece() != null){
            ChessPiece promotion = new ChessPiece(currentPlayer, move.getPromotionPiece());
            if(isAValidMove(move)){
                if(gameBoard.getPiece(move.getStartPosition()).getTeamColor() != currentPlayer){
                    throw new InvalidMoveException();
                }
                gameBoard.commitMove(move, promotion);
                setTeamTurn(enemy(currentPlayer));
            }
            else{
                throw new InvalidMoveException();
            }
        }
        else {
            if(isAValidMove(move)){
                if(gameBoard.getPiece(move.getStartPosition()).getTeamColor() != currentPlayer){
                    throw new InvalidMoveException();
                }
                gameBoard.commitMove(move, piece);
                setTeamTurn(enemy(currentPlayer));
            }
            else{
                throw new InvalidMoveException();
            }
        }
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        ChessPosition king = findKing(teamColor, gameBoard);
        Collection<ChessMove> enemyMoves = teamMoves(enemy(teamColor));
        if(king == null){
            return true;
        }
        for(ChessMove move : enemyMoves){
            ChessPosition attack = move.getEndPosition();
            if(attack.getRow() == king.getRow() && attack.getColumn() == king.getColumn()){
                return true;
            }
        }

        return false;
    }

    public boolean isAValidMove(ChessMove move){
        ChessBoard boardCopy = gameBoard.clone();

        assert boardCopy.equals(gameBoard);
        if(gameBoard.getPiece(move.getStartPosition()) == null){ /*No piece to move */
            return false;
        }
        TeamColor color = gameBoard.getPiece(move.getStartPosition()).getTeamColor();
        Collection<ChessMove> possibleMoves = gameBoard.getPiece(move.getStartPosition()).pieceMoves(gameBoard,
                move.getStartPosition());
        ChessPosition endPOS = move.getEndPosition();
        if(gameBoard.getPiece(move.getEndPosition()) != null &&
                gameBoard.getPiece(move.getEndPosition()).getTeamColor() == color){ /* Can't capture own piece */
            return false;
        }

        if(!possibleMoves.contains(move)){ /*You can't move your piece beyond what it can do */
            return false;
        }
        gameBoard.commitMove(move, gameBoard.getPiece(move.getStartPosition()));
        if(isInCheck(color)){ /*Can't put yourself into check nor keep yourself in check */
            this.setBoard(boardCopy);
            return false;
        }

        this.setBoard(boardCopy);


        return true;

    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        Collection<ChessMove> allyMoves = teamMoves(teamColor); /* Every move you can make */
        if(isInCheck(teamColor)){ /*Are we in a stalemate or not */
            for(ChessMove moves: allyMoves){
                if(isAValidMove(moves)){ /*Is there a move you can make that does not break the rules and also gets you out of check */
                    return false;
                }

            }
            return true;
        }
        return false; /*if you're not in check, are you in a stalemate? */

    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        Collection<ChessMove> allyMoves = teamMoves(teamColor); /*All the moves your pieces can make */
        if(isInCheck(teamColor)){
            return false;
        }
        for(ChessMove moves: allyMoves){
            if(isAValidMove(moves)){
                return false; /*if any of them are valid, you can do something, so it's not a stalemate */
            }

        }
        return true; /* otherwise you can't move, but you aren't in check, so it's a stalemate*/
    }

    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        gameBoard = board;
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return gameBoard;
    }

    public ChessPosition findKing(TeamColor kingColor, ChessBoard board){
        for(int k = 1; k <= 8; k++ ){
            for(int i = 1; i <= 8; i++){
                ChessPosition testPOS = new ChessPosition(k, i);
                if(board.getPiece(testPOS) != null && board.getPiece(testPOS).getPieceType() == ChessPiece.PieceType.KING){
                    if(board.getPiece(testPOS).getTeamColor() == kingColor){
                        return testPOS;
                    }
                }
            }
        }
        return null;
    }
    public Collection<ChessMove> teamMoves(TeamColor team){
        Collection<ChessMove> validTeamMoves = new ArrayList<>();
        for(int k = 1; k <= 8; k++ ){
            for(int i = 1; i <= 8; i++){
                ChessPosition testPOS = new ChessPosition(k, i);
                if(gameBoard.getPiece(testPOS) != null && gameBoard.getPiece(testPOS).getTeamColor() == team){
                    ChessPiece checkMoves = gameBoard.getPiece(testPOS);
                    validTeamMoves.addAll(checkMoves.pieceMoves(gameBoard, testPOS));
                }
            }
        }
        return validTeamMoves;
    }

    public TeamColor enemy(TeamColor color){
        if(color == TeamColor.WHITE){
            return TeamColor.BLACK;
        }
        else {
            return  TeamColor.WHITE;
        }
    }

}
