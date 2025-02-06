package chess;

import java.util.ArrayList;
import java.util.Collection;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {
    private ChessBoard game_board = new ChessBoard();
    private TeamColor current_player = TeamColor.WHITE;
    private TeamColor teamCheck;

    public ChessGame() {
        game_board.resetBoard();

    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return current_player;
    }

    /**
     * Set's which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {

        current_player = team;
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
        ChessPiece currentPiece = game_board.getPiece(startPosition);

        if(game_board.getPiece(startPosition) == null){
            return null;
        }
        if(teamCheck != current_player){
            validMoves.addAll(currentPiece.pieceMoves(game_board, startPosition));
            /*Piece can go anywhere so long as it doesn't put king in check */
        }
        if(teamCheck == current_player){
            /*Can only move to get out of Check */
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
        if(move.getPromotionPiece() != null){
            ChessPiece promotion = new ChessPiece(current_player, move.getPromotionPiece());
            game_board.commitMove(move, promotion);
        }
        else game_board.commitMove(move, game_board.getPiece(move.getStartPosition()));
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        ChessPosition king = findKing(teamColor);
        Collection<ChessMove> enemyMoves = TeamMoves(enemy(teamColor));
        for(ChessMove move : enemyMoves){
            ChessPosition attack = move.getEndPosition();
            if(attack.getRow() == king.getRow() && attack.getColumn() == king.getColumn()){
                teamCheck = teamColor;
                return true;
            }
        }

        return false;
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        ChessPosition kingSpot = findKing(teamColor);
        Collection<ChessMove> enemyMoves = TeamMoves(enemy(teamColor));
        Collection<ChessMove> allyMoves = TeamMoves(teamColor);
        return false;
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        Collection<ChessMove> allyMoves = TeamMoves(teamColor);
        return allyMoves == null;
    }

    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
       game_board = board;
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return game_board;
    }

    public ChessPosition findKing(TeamColor kingColor){
        for(int k = 1; k <= 8; k++ ){
            for(int i = 1; i <= 8; i++){
                ChessPosition testPOS = new ChessPosition(k, i);
                if(game_board.getPiece(testPOS) != null && game_board.getPiece(testPOS).getPieceType() == ChessPiece.PieceType.KING){
                    if(game_board.getPiece(testPOS).getTeamColor() == kingColor){
                        return testPOS;
                    }
                }
            }
        }
        return null;
    }
    public Collection<ChessMove> TeamMoves(TeamColor team){
        Collection<ChessMove> validTeamMoves = new ArrayList<>();
        for(int k = 1; k <= 8; k++ ){
            for(int i = 1; i <= 8; i++){
                ChessPosition testPOS = new ChessPosition(k, i);
                if(game_board.getPiece(testPOS) != null && game_board.getPiece(testPOS).getTeamColor() == team){
                    ChessPiece checkMoves = game_board.getPiece(testPOS);
                    validTeamMoves.addAll(checkMoves.pieceMoves(game_board, testPOS));
                }
            }
        }
        return validTeamMoves;
    }

    public TeamColor enemy(TeamColor color){
        if(color == TeamColor.WHITE){
            return TeamColor.BLACK;
        }
        else return  TeamColor.WHITE;
    }

}
