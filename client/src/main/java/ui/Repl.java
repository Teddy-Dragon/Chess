package ui;

import static ui.EscapeSequences.*;

public class Repl {
    public Repl(String serverURL){
        ServerFacade client = new ServerFacade(serverURL);
    }

    public void run(){
        System.out.println(SET_TEXT_COLOR_BLACK + "Welcome to Chess");


    }
}
