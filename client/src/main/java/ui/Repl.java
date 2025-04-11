package ui;

import model.Notification;
import websocket.messages.ServerMessage;

import java.util.Objects;
import java.util.Scanner;

import static ui.EscapeSequences.*;

public class Repl implements NotificationHandler {
    String serverURL;
    public Repl(String serverURL){
        this.serverURL = serverURL;

    }

    public void run(){
        HelpDisplay helpDisplay = new HelpDisplay(null);
        ChessClient eval = new ChessClient(serverURL, this);
        System.out.println(SET_TEXT_COLOR_BLACK + SET_BG_COLOR_LIGHT_GREY + " Welcome to Chess" + " "+  RESET_BG_COLOR + RESET_TEXT_COLOR);
        Scanner scanner = new Scanner(System.in);
        var input = "";
        System.out.println(helpDisplay);
        while (!Objects.equals(input, "quit")){
           String line = scanner.nextLine();
           System.out.println(SET_TEXT_COLOR_LIGHT_GREY + ">>>" + line + "<<<" + RESET_TEXT_COLOR);
           input = eval.eval(line);

           System.out.println(input);
        }

    }


    public void notify(Notification notification) {

    }
}
