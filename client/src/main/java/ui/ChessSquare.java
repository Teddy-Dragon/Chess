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

    public String rowTopOrBottom(Boolean whiteFirst){
        String response = "";
        List<String> firstColor = Arrays.asList(SET_BG_COLOR_WHITE, SET_BG_COLOR_BLACK);
        if(!whiteFirst){
            firstColor = firstColor.reversed();
        }
        for(int i = 0; i < 4; i++){
            response += chessTopOrBottom(firstColor.get(0));
            response += chessTopOrBottom(firstColor.get(1));
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

    public String rowChessMiddle(ChessPiece[] boardRow, ChessGame.TeamColor playerColor, Boolean whiteFirst){
        // include null for empty squares PLEASE
        String sideLabels = sideLabelMaker(playerColor);
        List<String> rowPieces = new ArrayList<>();
        if(playerColor == ChessGame.TeamColor.BLACK){
            for(int i = 7; i >= 0; i--){
                rowPieces.add(getPiece(boardRow, i));

            }
        }else{
            for(int i = 0; i < 8; i++){
                rowPieces.add(getPiece(boardRow, i));
            }
        }

        String squareColor = null;
        String altSquareColor = null;
        if(whiteFirst){
            squareColor = SET_BG_COLOR_WHITE;
            altSquareColor = SET_BG_COLOR_BLACK;
        }
        else{
            squareColor = SET_BG_COLOR_BLACK;
            altSquareColor= SET_BG_COLOR_WHITE;
        }

        String response = "";
        for(int i = 0; i < rowPieces.size(); i += 2){
            response += chessMiddle(rowPieces.get(i), squareColor);
            response += chessMiddle(rowPieces.get(i + 1), altSquareColor);
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
