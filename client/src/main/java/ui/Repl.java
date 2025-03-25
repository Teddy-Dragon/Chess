package ui;

import static ui.EscapeSequences.*;

public class Repl {
    public Repl(String serverURL){
        ServerFacade client = new ServerFacade(serverURL);
    }

    public void run(){
        ClientUI display = new ClientUI();
        System.out.println(SET_TEXT_COLOR_BLACK + SET_BG_COLOR_LIGHT_GREY + " Welcome to Chess" + " "+  RESET_BG_COLOR);
        display.help();



    }
}
