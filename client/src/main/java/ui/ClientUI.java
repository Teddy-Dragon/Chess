package ui;

import chess.ChessGame;

import java.util.Arrays;
import java.util.List;

import static ui.EscapeSequences.*;

public class ClientUI {




    public void help(){
        System.out.println(SET_TEXT_COLOR_BLACK + SET_BG_COLOR_LIGHT_GREY +  " This is the help display " + RESET_BG_COLOR);
    }

    public void chessBoardDisplay(ChessGame.TeamColor playerColor){
        String whiteSquareEdge = SET_BG_COLOR_WHITE + EMPTY + " " + EMPTY + RESET_BG_COLOR;
        String blackSquareEdge = SET_BG_COLOR_BLACK + EMPTY + " " + EMPTY + RESET_BG_COLOR;
        chessLabels(playerColor);
        String top = "";
        for(int z = 0; z < 4; z++){
            top += whiteSquareEdge;
            top += blackSquareEdge;
        }
        System.out.println(top);
        String middle = "";
        for(int i = 0; i < 4; i++){
            middle += whiteSquareToken(" ");
            middle += blackSquareToken(" ");
        }
        System.out.println(middle);
        String bottom = "";
        for(int z = 0; z < 4; z++){
            bottom += whiteSquareEdge;
            bottom += blackSquareEdge;
        }


    }

    public String whiteSquareToken(String Token){
        return SET_BG_COLOR_WHITE + EMPTY + SET_TEXT_COLOR_BLACK + Token + EMPTY + RESET_BG_COLOR;
    }
    public String blackSquareToken(String Token){
        return SET_BG_COLOR_BLACK + EMPTY + SET_TEXT_COLOR_WHITE + Token + EMPTY + RESET_BG_COLOR;
    }

    public void chessLabels(ChessGame.TeamColor playerColor){
        String numberLine = "";


        List<String> boardLetters = Arrays.asList("a", "b", "c", "d", "e", "f", "g", "h");
        List<String> boardNumbers = Arrays.asList("1", "2", "3", "4", "5", "6", "7", "8");
        if(playerColor != ChessGame.TeamColor.WHITE){
            boardLetters = boardLetters.reversed();
            boardNumbers = boardNumbers.reversed();

        }
        for (String boardLetter : boardLetters) {
            numberLine += SET_BG_COLOR_LIGHT_GREY + EMPTY + boardLetter + EMPTY + RESET_BG_COLOR;
        }
        System.out.println("\n" + numberLine);
    }

}
