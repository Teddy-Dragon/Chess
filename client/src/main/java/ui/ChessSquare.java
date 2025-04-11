package ui;

import chess.ChessGame;
import chess.ChessPiece;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static ui.EscapeSequences.*;

public class ChessSquare {
    private int rowsPrinted = 0;

    public String chessTopOrBottom(String backgroundColor){
        return backgroundColor + EMPTY + " " + EMPTY + RESET_BG_COLOR;
    }

    public String rowTopOrBottom(Boolean whiteFirst, List<Integer> rowHighlights, ChessGame.TeamColor playerColor){
        String response = "";
        if(playerColor == ChessGame.TeamColor.BLACK){
            rowHighlights = invertColumnAndSubtract(rowHighlights, true);
        }
        else{
            rowHighlights = invertColumnAndSubtract(rowHighlights, false);
        }
        List<String> firstColor = Arrays.asList(SET_BG_COLOR_WHITE, SET_BG_COLOR_BLACK);
        List<String> highlightColor = Arrays.asList(SET_BG_COLOR_GREEN, SET_BG_COLOR_DARK_GREEN);
        if(!whiteFirst){
            firstColor = firstColor.reversed();
            highlightColor = highlightColor.reversed();
        }
        for(int i = 0; i < 8; i+= 2){
            response += highlightRowTopOrBottom(i, rowHighlights, firstColor.get(0), highlightColor.get(0));
            response += highlightRowTopOrBottom(i + 1, rowHighlights, firstColor.get(1), highlightColor.get(1));
        }

        return SET_BG_COLOR_LIGHT_GREY + "   " + RESET_BG_COLOR + response + SET_BG_COLOR_LIGHT_GREY + "   " + RESET_BG_COLOR;
    }

    private String highlightRowTopOrBottom(int col, List<Integer> sortedColumns, String color, String highlightColor){
        String response = "";

        if(!sortedColumns.isEmpty() && sortedColumns.contains(col)){
            response += chessTopOrBottom(highlightColor);
        }
        else{
            response += chessTopOrBottom(color);
        }
        return response;

    }

    public String chessMiddle(String pieceType, String squareColor){

        return squareColor + EMPTY + pieceType + EMPTY + RESET_BG_COLOR;
    }

    public String getPiece(ChessPiece[] boardRow, int location){
        String piece = "";
        if(boardRow[location] != null && boardRow[location].getTeamColor() == ChessGame.TeamColor.WHITE){
            piece += SET_TEXT_COLOR_RED;
        }
        if(boardRow[location]!= null && boardRow[location].getTeamColor() == ChessGame.TeamColor.BLACK){
            piece += SET_TEXT_COLOR_BLUE;
        }
        piece += getChessLetter(boardRow, location) + RESET_TEXT_COLOR;
        return piece;
    }

    public String rowChessMiddle(ChessPiece[] boardRow, ChessGame.TeamColor playerColor, Boolean whiteFirst, List<Integer> rowHighlights){
        // include null for empty squares PLEASE
        String sideLabels = sideLabelMaker(playerColor);
        List<String> rowPieces = new ArrayList<>();
        if(playerColor == ChessGame.TeamColor.BLACK){
            rowHighlights = invertColumnAndSubtract(rowHighlights, true);
            for(int i = 7; i >= 0; i--){
                rowPieces.add(getPiece(boardRow, i));

            }
        }else{
            rowHighlights = invertColumnAndSubtract(rowHighlights, false);
            for(int i = 0; i < 8; i++){
                rowPieces.add(getPiece(boardRow, i));
            }
        }
        String squareColor = null;
        String altSquareColor = null;
        String highlightColor = null;
        String altHighlightColor = null;
        if(whiteFirst){
            squareColor = SET_BG_COLOR_WHITE;
            altSquareColor = SET_BG_COLOR_BLACK;
            highlightColor = SET_BG_COLOR_GREEN;
            altHighlightColor = SET_BG_COLOR_DARK_GREEN;
        }
        else{
            squareColor = SET_BG_COLOR_BLACK;
            altSquareColor= SET_BG_COLOR_WHITE;
            highlightColor = SET_BG_COLOR_DARK_GREEN;
            altHighlightColor = SET_BG_COLOR_GREEN;
        }

        String response = "";
        for(int i = 0; i < rowPieces.size(); i += 2){
            response += highlightMiddle(rowHighlights, rowPieces.get(i), i, squareColor, highlightColor);
            response += highlightMiddle(rowHighlights, rowPieces.get(i + 1), i+1, altSquareColor, altHighlightColor);
        }

        return sideLabels+ response + sideLabels;
    }

    private String highlightMiddle(List<Integer> sortedNumbers, String rowPiece, int colLocation, String color, String highlightColor){
        String response = "";
        if(!sortedNumbers.isEmpty() && sortedNumbers.contains(colLocation)){
            response += chessMiddle(rowPiece, highlightColor);
        }
        else{
            response += chessMiddle(rowPiece, color);
        }
        return response;
    }

    private String sideLabelMaker(ChessGame.TeamColor playerColor){
        List<String> boardNumbers = Arrays.asList("1", "2", "3", "4", "5", "6", "7", "8");
        if(playerColor != ChessGame.TeamColor.BLACK){
            boardNumbers = boardNumbers.reversed();
        }
        String response = SET_BG_COLOR_LIGHT_GREY + " "+ SET_TEXT_COLOR_BLACK + boardNumbers.get(rowsPrinted) + " " +  RESET_BG_COLOR;
        rowsPrinted += 1;
        return response;
    }

    private List<Integer> invertColumnAndSubtract(List<Integer> columns, Boolean invert){
        List<Integer> invertedColumns = new ArrayList<>();
        if(columns == null || columns.isEmpty()){ // if there's nothing in the list
            return invertedColumns;
        }
        List<Integer> invertedNumbers = Arrays.asList(7, 6, 5, 4, 3, 2, 1, 0);
        if(invert){//if we're switching order as well as adjusting the system
            for(Integer col: columns){
                invertedColumns.add(invertedNumbers.get(col - 1));
            }
        }
        else{ // if we're not, and we're just converting from the ChessPosition system to the usual 0-7
            for(Integer col: columns){
                invertedColumns.add(col - 1);
            }
        }
        return invertedColumns;
    }

    private String getChessLetter(ChessPiece[] boardRow, int location){
        if(boardRow[location] != null){
            switch (boardRow[location].getPieceType()){
                case QUEEN -> {
                    return "Q";
                }
                case BISHOP -> {
                    return "B";
                }
                case ROOK -> {
                    return "R";
                }
                case KNIGHT -> {
                    return "N";
                }
                case KING -> {
                    return "K";
                }
                case PAWN -> {
                    return "P";
                }
                case null, default -> {
                    return " ";
                }
            }
        }
        return " ";
    }
}