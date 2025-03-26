package ui;

import model.UserData;

import java.util.Arrays;
import java.util.UUID;

public class ClientEval {
    private String input;
    private UUID authorization;
    private ServerFacade client;
    public ClientEval(UUID authorization, ServerFacade client){
        this.input = input;
        this.authorization = authorization;
        this.client = client;
    }

    public String eval(String input){
       try{
            var tokens = input.toLowerCase().split(" ");
            if(tokens.length == 0) {
                helpEval();
                return "help";
            }
            String command = tokens[0];
            var parameters = Arrays.copyOfRange(tokens, 1, tokens.length);
            return switch (command){
                case "login" -> loginEval(parameters);
                case "logout" -> logoutEval();
                case "quit" -> "quit";
                case "register" -> registerEval(parameters);
                case "join" -> joinEval(parameters);
                case "list" -> listEval();
                case "create" -> createEval(parameters);
                case "watch" -> watchEval(parameters);
                default -> helpEval();

            };
       }catch(Exception e){
           return e.toString();
       }


    }
    public String registerEval(String[] parameters){
        if(parameters.length < 3){
            return "Not enough arguments";
        }
        if(parameters.length > 3){
            return "Too many arguments";
        }
        String username = parameters[0];
        String password = parameters[1];
        String email = parameters[2];
        UserData newUser = new UserData(username, password, email);
        client.registerUser(newUser);
        System.out.println(client.getAuth());
        authorization = client.getAuth();
        return null;
    }
    public String loginEval(String[] parameters){
        if(parameters.length < 2){
            return "Not enough arguments";
        }
        if(parameters.length > 2){
            return "Too many arguements";
        }
        String username = parameters[0];
        String password = parameters[1];
        UserData returningUser = new UserData(username, password, null);
        client.loginUser(returningUser);
        authorization = client.getAuth();

        return null;
    }
    public String watchEval(String[] parameters){
        return null;
    }

    public String joinEval(String[] parameters){
        return null;
    }
    public String createEval(String[] parameters){
        return null;
    }

    public String logoutEval(){
        if(authorization != null){
            client.logoutUser();
            return "Logout Successful";
        }else return "Not logged in";

    }
    public String listEval(){
        return null;
    }

    public String helpEval(){
        return new ClientUI(authorization).helpDisplay();
    }
    public String formatResponse(){
        return null;
    }


}
