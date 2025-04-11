package ui;

import java.util.UUID;

import static ui.EscapeSequences.RESET_TEXT_COLOR;
import static ui.EscapeSequences.SET_TEXT_COLOR_MAGENTA;

public class HelpDisplay {
    private UUID authorization;
    public HelpDisplay(UUID authorization){
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

}
