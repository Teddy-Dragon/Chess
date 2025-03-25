package ui;

import java.util.Scanner;

import static ui.EscapeSequences.*;

public class Repl {
    ServerFacade client;
    public Repl(String serverURL){
        client = new ServerFacade(serverURL);
    }

    public void run(){
        ClientUI display = new ClientUI(client.getAuth());
        System.out.println(SET_TEXT_COLOR_BLACK + SET_BG_COLOR_LIGHT_GREY + " Welcome to Chess" + " "+  RESET_BG_COLOR);
        display.helpDisplay();

        Scanner scanner = new Scanner(System.in);
        var input = "";




    }
}
