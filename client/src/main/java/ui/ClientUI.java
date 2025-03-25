package ui;

import chess.ChessGame;

import java.util.Arrays;
import java.util.List;

import static ui.EscapeSequences.*;

public class ClientUI {
    public String textColor;




    public void help(){
        System.out.println(SET_TEXT_COLOR_BLACK + SET_BG_COLOR_LIGHT_GREY +  " This is the help display " + RESET_BG_COLOR);
    }

    public void chessBoardDisplay(ChessGame.TeamColor playerColor){
        if(playerColor == ChessGame.TeamColor.WHITE){
            textColor = SET_TEXT_COLOR_BLUE;
        }
        else{
            textColor = SET_TEXT_COLOR_RED;
        }
        topLabel(playerColor);

        ChessSquare row = new ChessSquare();

        System.out.println(row.rowTopOrBottom(true));
        System.out.println(row.rowChessMiddle(beginningRowTypes(1), playerColor, true));
        System.out.println(row.rowTopOrBottom(true));
        System.out.println(row.rowTopOrBottom(false));
        System.out.println(row.rowChessMiddle(beginningRowTypes(2), playerColor, false));
        System.out.println(row.rowTopOrBottom(false));
        Boolean whitefirst = true;
        for(int i = 0; i < 4; i++){
            System.out.println(row.rowTopOrBottom(whitefirst));
            System.out.println(row.rowChessMiddle(beginningRowTypes(3), playerColor, whitefirst));
            System.out.println(row.rowTopOrBottom(whitefirst));
            whitefirst = !whitefirst;
        }
        if(textColor.equals(SET_TEXT_COLOR_BLUE)){
            textColor = SET_TEXT_COLOR_RED;
        }
        else{
            textColor = SET_TEXT_COLOR_BLUE;
        }
        System.out.println(row.rowTopOrBottom(true));
        System.out.println(row.rowChessMiddle(beginningRowTypes(2), enemyColor(playerColor), true));
        System.out.println(row.rowTopOrBottom(true));
        System.out.println(row.rowTopOrBottom(false));
        System.out.println(row.rowChessMiddle(beginningRowTypes(1), enemyColor(playerColor), false));
        System.out.println(row.rowTopOrBottom(false));
        topLabel(playerColor);
    }


    public void topLabel(ChessGame.TeamColor playerColor){
            String numberLine = "";
            List<String> boardLetters = Arrays.asList("a", "b", "c", "d", "e", "f", "g", "h");

            if (playerColor != ChessGame.TeamColor.WHITE) {
                boardLetters = boardLetters.reversed();
            }
            for (String boardLetter : boardLetters) {
                numberLine += SET_BG_COLOR_LIGHT_GREY + EMPTY + boardLetter + EMPTY + RESET_BG_COLOR;
            }
            System.out.println(SET_BG_COLOR_LIGHT_GREY + "   " + numberLine + SET_BG_COLOR_LIGHT_GREY + "   " + RESET_BG_COLOR);


    }
    public ChessGame.TeamColor enemyColor(ChessGame.TeamColor playerColor){
        if(playerColor != ChessGame.TeamColor.WHITE){
            return ChessGame.TeamColor.BLACK;
        }
        else return ChessGame.TeamColor.WHITE;
    }

    public List<String> beginningRowTypes(int whichType){
        List<String> startingPieces = Arrays.asList(textColor + "R", textColor + "N", textColor + "B", textColor + "K",
                textColor + "Q", textColor + "B", textColor +  "N", textColor+  "R");
        List<String> pawnRow = Arrays.asList(textColor + "P", textColor + "P", textColor + "P", textColor + "P",
                textColor + "P", textColor + "P", textColor +  "P", textColor+  "P");
        List<String> emptyRow = Arrays.asList(" ", " ", " ", " ", " ", " ", " ", " ");
        if(whichType == 1){
            return startingPieces;
        } else if (whichType == 2) {
            return pawnRow;
        }
        else return emptyRow;
    }

}
