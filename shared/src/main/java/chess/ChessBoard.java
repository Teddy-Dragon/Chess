package chess;

/**
 * A chessboard that can hold and rearrange chess pieces.
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessBoard {
    private ChessPiece[][] squares = new ChessPiece[8][8];
    public ChessBoard() {

    }

    /**
     * Adds a chess piece to the chessboard
     *
     * @param position where to add the piece to
     * @param piece    the piece to add
     */
    public void addPiece(ChessPosition position, ChessPiece piece) {

        squares[position.getRow()][position.getColumn()] = piece;
    }

    /**
     * Gets a chess piece on the chessboard
     *
     * @param position The position to get the piece from
     * @return Either the piece at the position, or null if no piece is at that
     * position
     */
    public ChessPiece getPiece(ChessPosition position) {
        return squares[position.getRow()][position.getColumn()];
    }

    public int IsAvailable(ChessPosition position){
        /**
         * 1: Encountered another piece
         * 2: Out of bounds
         * 0: Available
         * **/
        if(getPiece(position) != null){
            return 1;
        }
        if(position.getRow() < 0 || position.getRow() > 8){
            return 2;
        }
        if(position.getColumn() < 0 || position.getColumn() > 8){
            return 2;
        }
        return 0;
    }

    /**
     * Sets the board to the default starting board
     * (How the game of chess normally starts)
     */
    public void resetBoard() {
        squares = new ChessPiece[8][8];
        for(int i = 1; i <= 8; i++){
            addPiece(new ChessPosition(2, i), new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN));
            addPiece(new ChessPosition(7, i), new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN));
        }
        for(int i = 1; i <= 8; i+= 7){
            addPiece(new ChessPosition(1, i), new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.ROOK));
            addPiece(new ChessPosition(8, i), new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.ROOK));

        }
        for(int i = 2; i <= 7; i += 5){
            addPiece(new ChessPosition(1, i), new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KNIGHT));
            addPiece(new ChessPosition(8, i), new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KNIGHT));
        }
        for(int i = 3; i <= 6; i+= 3){
            addPiece(new ChessPosition(1, i),new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.BISHOP));
            addPiece(new ChessPosition(8, i), new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.BISHOP));
        }
        addPiece(new ChessPosition(1, 4),new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.QUEEN));
        addPiece(new ChessPosition(8, 4), new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.QUEEN));
        addPiece(new ChessPosition(1, 5),new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KING));
        addPiece(new ChessPosition(8, 5), new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KING));

    }
}
