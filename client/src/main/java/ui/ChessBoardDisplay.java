package ui;

import chess.ChessGame;
import chess.ChessPiece;
import chess.ChessPosition;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static ui.EscapeSequences.*;

public class ChessBoardDisplay {
    public String textColor;

    public ChessBoardDisplay(){
    }



    public String chessBoardDisplay(ChessGame.TeamColor playerColor, ChessGame game, List<ChessPosition> highlights){
        if(playerColor == ChessGame.TeamColor.WHITE){
            textColor = SET_TEXT_COLOR_BLUE;
        }
        else{
            textColor = SET_TEXT_COLOR_RED;
        }
        ChessPiece[][] board = game.getBoard().getSquares();

        ChessSquare row = new ChessSquare();
        String displayChessBoard = "";
        displayChessBoard += "\n" + SET_TEXT_COLOR_BLACK + topLabel(playerColor);
        Boolean whiteFirst = true;

        if(playerColor == ChessGame.TeamColor.BLACK){
            for(int i = 0; i < 8; i++){ //We go from row 0 to row 7
                List<Integer> rowHighlights = getHighlightsInRow(highlights, i, false);
                // get a row, if there are valid moves in the highlights, get those as well

                displayChessBoard += row.rowTopOrBottom(whiteFirst, rowHighlights, ChessGame.TeamColor.BLACK) + "\n";
                // makes the top of a row
                displayChessBoard += row.rowChessMiddle(board[i], ChessGame.TeamColor.BLACK, whiteFirst, rowHighlights) + "\n";
                // makes the middle of a row
                displayChessBoard += row.rowTopOrBottom(whiteFirst, rowHighlights, ChessGame.TeamColor.BLACK) + "\n";
                // makes the bottom of a row
                whiteFirst = !whiteFirst; // tells the chessboard to flip color alternations
                rowHighlights.clear(); // if there were any highlights for this row, delete so it doesn't interfere with the next row
            }
        }
        if(playerColor == ChessGame.TeamColor.WHITE){

            for(int i = 7; i >= 0; i--){ // we go from row 7 to row 0
                List<Integer> rowHighlights = getHighlightsInRow(highlights, i, false);

                displayChessBoard += row.rowTopOrBottom(whiteFirst, rowHighlights, ChessGame.TeamColor.WHITE) + "\n";
                displayChessBoard += row.rowChessMiddle(board[i], ChessGame.TeamColor.WHITE, whiteFirst, rowHighlights) + "\n";
                displayChessBoard += row.rowTopOrBottom(whiteFirst, rowHighlights, ChessGame.TeamColor.WHITE) + "\n";
                whiteFirst = !whiteFirst;
            }
        }
        displayChessBoard += topLabel(playerColor);
        return displayChessBoard;

    }



    public String topLabel(ChessGame.TeamColor playerColor){
            String numberLine = "";
            List<String> boardLetters = Arrays.asList("a", "b", "c", "d", "e", "f", "g", "h");

            if (playerColor != ChessGame.TeamColor.WHITE) {
                boardLetters = boardLetters.reversed();
            }
            for (String boardLetter : boardLetters) {
                numberLine += SET_BG_COLOR_LIGHT_GREY + EMPTY + boardLetter + EMPTY + RESET_BG_COLOR;
            }
            return SET_BG_COLOR_LIGHT_GREY + "   " + numberLine + SET_BG_COLOR_LIGHT_GREY + "   " + RESET_BG_COLOR  + "\n";


    }

    public List<Integer> getHighlightsInRow(List<ChessPosition> highlights, int rowLocation, Boolean invert){
        List<Integer> invertedNumbers = Arrays.asList(8 ,7 , 6, 5, 4, 3, 2, 1);
        List<Integer> response = new ArrayList<>();
        List<ChessPosition> invertedHighlights = highlights;

        if(highlights != null && invert){
            invertedHighlights = new ArrayList<>();
            for(ChessPosition row: highlights){
                invertedHighlights.add(new ChessPosition(invertedNumbers.get(row.getRow() - 1), row.getColumn()));
            }
        }



        if(invertedHighlights != null){
            for(ChessPosition highlight: invertedHighlights){
                if(highlight.getRow() - 1 == rowLocation){
                    response.add(highlight.getColumn());
                }
            }
        }

        return response;

    };
}
