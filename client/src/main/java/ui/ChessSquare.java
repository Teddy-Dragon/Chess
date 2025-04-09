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

    public String rowTopOrBottom(Boolean whiteFirst,List<Integer> highlights ){
        String response = "";
        List<String> firstColor = Arrays.asList(SET_BG_COLOR_WHITE, SET_BG_COLOR_BLACK);
        List<String> highlightColors = Arrays.asList(SET_BG_COLOR_GREEN, SET_BG_COLOR_DARK_GREEN);
        if(!whiteFirst){
            firstColor = firstColor.reversed();
            highlightColors = highlightColors.reversed();
        }
        for(int i = 0; i != 8; i += 2){
            if(highlights != null && highlights.size() != 0){
                if(highlights.contains(i + 1)){
                    response += chessTopOrBottom(highlightColors.get(0));
                } else if (!highlights.contains(i + 1)) {
                    response += chessTopOrBottom(firstColor.get(0));
                }
                if(highlights.contains(i + 2)){
                    response += chessTopOrBottom(highlightColors.get(1));
                } else if (!highlights.contains(i + 2)) {
                    response += chessTopOrBottom(firstColor.get(1));
                }
            }
            else{
                response += chessTopOrBottom(firstColor.get(0));
                response += chessTopOrBottom(firstColor.get(1));
            }
        }

        return SET_BG_COLOR_LIGHT_GREY + "   " + RESET_BG_COLOR + response + SET_BG_COLOR_LIGHT_GREY + "   " + RESET_BG_COLOR;
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


    public String rowChessMiddle(ChessPiece[] boardRow, ChessGame.TeamColor playerColor, Boolean whiteFirst, List<Integer> highlights){
        // include null for empty squares PLEASE

        String sideLabels = sideLabelMaker(playerColor);
        List<String> rowPieces = new ArrayList<>();
        if(playerColor == ChessGame.TeamColor.WHITE){
            for(int i = 7; i >= 0; i--){
                rowPieces.add(getPiece(boardRow, i));

            }
        }else{
            for(int i = 0; i < 8; i++){
                rowPieces.add(getPiece(boardRow, i));
            }
        }
        String highlightColorOne = null;
        String highlightColorTwo = null;
        String squareColor = null;
        String altSquareColor = null;
        if(whiteFirst){
            squareColor = SET_BG_COLOR_WHITE;
            highlightColorOne = SET_BG_COLOR_GREEN;
            altSquareColor = SET_BG_COLOR_BLACK;
            highlightColorTwo = SET_BG_COLOR_DARK_GREEN;
        }
        else{
            squareColor = SET_BG_COLOR_BLACK;
            highlightColorOne = SET_BG_COLOR_DARK_GREEN;
            altSquareColor= SET_BG_COLOR_WHITE;
            highlightColorTwo = SET_BG_COLOR_GREEN;
        }

        String response = "";
        for(int i = 0; i < rowPieces.size(); i += 2){
            if(highlights == null && highlights.size() == 0){
                response += chessMiddle(rowPieces.get(i), squareColor);
                response += chessMiddle(rowPieces.get(i + 1), altSquareColor);
            }
            else{
                if(highlights.contains(i)) {
                    response += chessMiddle(rowPieces.get(i), highlightColorOne);
                } else if (!highlights.contains(i)) {
                    response += chessMiddle(rowPieces.get(i), squareColor);
                }
                if(highlights.contains(i + 1)){
                    response += chessMiddle(rowPieces.get(i), highlightColorTwo);
                }
                else if(!highlights.contains( i + 1)){
                    response += chessMiddle(rowPieces.get(i + 1), altSquareColor);
                }

            }

        }

        return sideLabels+ response + sideLabels;
    }

    private String sideLabelMaker(ChessGame.TeamColor playerColor){
        List<String> boardNumbers = Arrays.asList("1", "2", "3", "4", "5", "6", "7", "8");
        if(playerColor != ChessGame.TeamColor.WHITE){
            boardNumbers = boardNumbers.reversed();
        }
        String response = SET_BG_COLOR_LIGHT_GREY + " "+ SET_TEXT_COLOR_BLACK + boardNumbers.get(rowsPrinted) + " " +  RESET_BG_COLOR;
        rowsPrinted += 1;
        return response;
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
