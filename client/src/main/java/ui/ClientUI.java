package ui;

import chess.ChessGame;
import chess.ChessPiece;
import model.GameData;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static ui.EscapeSequences.*;

public class ClientUI {
    public String textColor;
    private UUID authorization;
    public ClientUI(UUID authorization){
        this.authorization = authorization;
    }




    public String helpDisplay(){
        String response = SET_TEXT_COLOR_MAGENTA + "";
        if(authorization != null){
            response += "Options: \n";
            response += "To logout, type 'logout' \n";
            response += "To create a game type 'create' <gameName>\n";
            response += "To join a game, type 'join' <gameNumber> <playerColor>\n";
            response += "To list all games, type 'list'\n";
            response += "To watch a game, type watch <gameNumber> <playerColor>\n";
            response += "To exit, type 'quit'\n";
            response += "To repeat this helpful screen, just type 'help'\n"+ RESET_TEXT_COLOR;
            return response;
        } else {
            response += "Options: \n";
            response += "To login, type 'login' and then <USERNAME> <PASSWORD> Spaces are important for all options <3 \n";
            response += "To register as a new user, type 'register' and then type <USERNAME> <PASSWORD> <EMAIL> \n";
            response += "To exit, type 'quit'\n";
            response += "To repeat this helpful screen, just type 'help'\n"+ RESET_TEXT_COLOR;
            return response;
        }
    }

    public String chessBoardDisplay(ChessGame.TeamColor playerColor, GameData gameInfo){
        if(playerColor == ChessGame.TeamColor.WHITE){
            textColor = SET_TEXT_COLOR_BLUE;
        }
        else{
            textColor = SET_TEXT_COLOR_RED;
        }
        ChessPiece[][] board = gameInfo.game().getBoard().getSquares();


        ChessSquare row = new ChessSquare();
        String displayChessBoard = "";
        displayChessBoard += "\n" + SET_TEXT_COLOR_BLACK + topLabel(playerColor);
        Boolean whiteFirst = true;

        if(playerColor == ChessGame.TeamColor.BLACK){
            for(int i = 7; i >= 0; i--){
                displayChessBoard += row.rowTopOrBottom(whiteFirst);
                displayChessBoard += row.rowChessMiddle(board[i], ChessGame.TeamColor.BLACK, whiteFirst);
                displayChessBoard += row.rowTopOrBottom(whiteFirst);
                whiteFirst = !whiteFirst;
            }
        }
        if(playerColor == ChessGame.TeamColor.WHITE){
            for(int i = 0; i < board.length; i++){
                displayChessBoard += row.rowTopOrBottom(whiteFirst);
                displayChessBoard += row.rowChessMiddle(board[i], ChessGame.TeamColor.WHITE, whiteFirst);
                displayChessBoard += row.rowTopOrBottom(whiteFirst);
                whiteFirst = !whiteFirst;
            }
        }
//

//
//        displayChessBoard += row.rowTopOrBottom(true) + "\n";
//        displayChessBoard += row.rowChessMiddle(beginningRowTypes(1), playerColor, true) + "\n";
//        displayChessBoard += row.rowTopOrBottom(true) + "\n";
//        displayChessBoard += row.rowTopOrBottom(false) + "\n";
//        displayChessBoard += row.rowChessMiddle(beginningRowTypes(2), playerColor, false) + "\n";
//        displayChessBoard +=row.rowTopOrBottom(false) + "\n";
//
//        for(int i = 0; i < 4; i++){
//            displayChessBoard += row.rowTopOrBottom(whitefirst) + "\n";
//            displayChessBoard += row.rowChessMiddle(beginningRowTypes(3), playerColor, whitefirst) + "\n";
//            displayChessBoard += row.rowTopOrBottom(whitefirst) + "\n";
//            whitefirst = !whitefirst;
//        }
//        if(textColor.equals(SET_TEXT_COLOR_BLUE)){
//            textColor = SET_TEXT_COLOR_RED;
//        }
//        else{
//            textColor = SET_TEXT_COLOR_BLUE;
//        }
//        displayChessBoard += row.rowTopOrBottom(true) + "\n";
//        displayChessBoard += row.rowChessMiddle(beginningRowTypes(2), enemyColor(playerColor), true) + "\n";
//        displayChessBoard += row.rowTopOrBottom(true) + "\n";
//        displayChessBoard += row.rowTopOrBottom(false) + "\n";
//        displayChessBoard += row.rowChessMiddle(beginningRowTypes(1), enemyColor(playerColor), false) + "\n";
//        displayChessBoard += row.rowTopOrBottom(false) + "\n";
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
