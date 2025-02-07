package chess;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {
    private ChessBoard game_board = new ChessBoard();
    private TeamColor current_player = TeamColor.WHITE;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessGame chessGame = (ChessGame) o;
        return Objects.equals(game_board, chessGame.game_board) && current_player == chessGame.current_player;
    }

    @Override
    public int hashCode() {
        return Objects.hash(game_board, current_player);
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
        ChessPiece piece = game_board.getPiece(startPosition);
        Collection<ChessMove> possibleMoves = piece.pieceMoves(game_board, startPosition);
        if(game_board.getPiece(startPosition) == null){
            return null;
        }
        for(ChessMove move: possibleMoves){ /*For all moves check to see if it puts the king in check */
            if(isAValidMove(move, current_player)){
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
        ChessPiece piece = game_board.getPiece(move.getStartPosition());
        if(move.getPromotionPiece() != null){
            ChessPiece promotion = new ChessPiece(current_player, move.getPromotionPiece());
            if(isAValidMove(move, current_player)){
                game_board.commitMove(move, promotion);
                setTeamTurn(enemy(current_player));
            }
            else{
                throw new InvalidMoveException();
            }
        }else {
            if(isAValidMove(move, current_player)){
                game_board.commitMove(move, piece);
                setTeamTurn(enemy(current_player));
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
        ChessPosition king = findKing(teamColor, game_board);
        Collection<ChessMove> enemyMoves = TeamMoves(enemy(teamColor));
        for(ChessMove move : enemyMoves){
            ChessPosition attack = move.getEndPosition();
            if(attack.getRow() == king.getRow() && attack.getColumn() == king.getColumn()){
                return true;
            }
        }

        return false;
    }

    public boolean isAValidMove(ChessMove move, TeamColor color){
        ChessBoard boardCopy = game_board.clone();
        assert boardCopy.equals(game_board);
        if(game_board.getPiece(move.getStartPosition()) == null){
            return false;
        }
        Collection<ChessMove> possibleMoves = game_board.getPiece(move.getStartPosition()).pieceMoves(game_board, move.getStartPosition());
        ChessPosition endPOS = move.getEndPosition();
        if(game_board.getPiece(move.getEndPosition()) != null && game_board.getPiece(move.getEndPosition()).getTeamColor() == color){ /* Can't capture own piece */
            return false;
        }
        if(game_board.getPiece(move.getStartPosition()).getTeamColor() != color){ /* Can't move a piece that isn't yours or that doesn't exist */
            return false;
        }
        if(!possibleMoves.contains(move)){ /*You can't move your piece beyond what it can do */
            return false;
        }

        game_board.commitMove(move, game_board.getPiece(move.getStartPosition()));

        if(isInCheck(current_player)){ /*Can't put yourself into check */
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
        ChessPosition kingSpot = findKing(teamColor, game_board);
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
        System.out.println(Arrays.deepToString(new String[]{"COPY " + board}));
        System.out.println(Arrays.deepToString(new String[]{"ORIGINAL " + game_board}));
        game_board = board;
        System.out.println(Arrays.deepToString(new String[]{"COPY AFTER " + board}));
        System.out.println(Arrays.deepToString(new String[]{"ORIGINAL AFTER " + game_board}));
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return game_board;
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
