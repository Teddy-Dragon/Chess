package ui;

import java.util.Objects;
import java.util.Scanner;

import static ui.EscapeSequences.*;

public class Repl {
    ServerFacade client;
    public Repl(String serverURL){
        client = new ServerFacade(serverURL);
    }

    public void run(){
        ClientUI display = new ClientUI(client.getAuth());
        ClientEval eval = new ClientEval(client.getAuth(), client);
        System.out.println(SET_TEXT_COLOR_BLACK + SET_BG_COLOR_LIGHT_GREY + " Welcome to Chess" + " "+  RESET_BG_COLOR);
        display.helpDisplay();

        Scanner scanner = new Scanner(System.in);
        var input = "";
        while (!Objects.equals(input, "quit")){
           String line = scanner.nextLine();
           input = eval.eval(line);
           System.out.println(input);
        }




    }


}
