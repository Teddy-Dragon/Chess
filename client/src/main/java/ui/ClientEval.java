package ui;

import chess.ChessGame;
import model.*;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static ui.EscapeSequences.*;

public class ClientEval {
    private String input;
    private UUID authorization;
    private ClientServerFacade client;
    public ClientEval(UUID authorization, ClientServerFacade client){
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
                case "quit" -> quitEval();
                case "register" -> registerEval(parameters);
                case "join" -> joinEval(parameters);
                case "list" -> listEval();
                case "create" -> createEval(parameters);
                case "watch" -> watchEval(parameters);
                case "clear" -> clearEval(parameters);
                default -> helpEval();

            };
       }catch(Exception e){
           return e.toString();
       }


    }
    public String quitEval(){
        if(client.getAuth() != null){
            return "Please log out to quit!";
        }
        else{
            return "quit";
        }
    }
    public String clearEval(String[] parameters){
        if(parameters.length != 1){
            return "Not authorized";
        }
        try {
            if (Objects.equals(parameters[0], "kayleesaidso")) {
                client.clearAll();
                return "If Kaylee says so";
            } else {
                return "Wrong password";
            }
        }
        catch(Exception e){
            return "Something went wrong";
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
        if(!email.matches("[A-z0-9]+[@][A-z0-9]+[.]{1}[A-z]{3}")){
            return "Incorrect email type!";
        }
        UserData newUser = new UserData(username, password, email);
        try{
            AuthData registered = client.registerUser(newUser);
            authorization = client.getAuth();
            return formatResponse(registered, AuthData.class);
        }catch(Exception e){
            return "";
        }

    }
    public String loginEval(String[] parameters){
        try{
        if(parameters.length < 2){
            return "Not enough arguments";
        }
        if(parameters.length > 2){
            return "Too many arguments";
        }
        String username = parameters[0];
        String password = parameters[1];
        UserData returningUser = new UserData(username, password, null);
        AuthData response = client.loginUser(returningUser);
        if(client.getAuth() != null){
            authorization = client.getAuth();
            return formatResponse(response, AuthData.class);
        }
        return "Error logging in! Try again!";}catch(Exception e){
            return "";
        }



    }
    public String watchEval(String[] parameters){
        try{
            ListModel games = new ListModel(client.listGames().games());
            if(parameters.length != 2){
                return "Wrong number of arguments";
            }
            if(authorization == null){
                return "Log in first";
            }
            if(!parameters[0].matches("[0-9]") && Integer.parseInt(parameters[0]) > games.games().size()){
                return "Not a game number";
            }
            if(!parameters[1].matches("white|black")){
                return "Not a watchable team";
            }
            int gameID = games.games().get(Integer.parseInt(parameters[0]) - 1).gameID();
            String playerColor = parameters[1];

                return playGame(playerColor, client.getGameByID(gameID));
        }catch(Exception e){
            return "";
        }

    }

    public String joinEval(String[] parameters){
        try{
        ListModel games = new ListModel(client.listGames().games());
        if(parameters.length != 2){
            return "Wrong number of arguments";
        }
        if(!parameters[0].matches("[0-9]") && Integer.parseInt(parameters[0]) > games.games().size()){
            return "Not a game number";
        }
        int gameNumber = games.games().get(Integer.parseInt(parameters[0]) - 1).gameID();
        if(!parameters[1].matches("white|black")){
            return "Not a playable team";
        }
        String playerColor = parameters[1];

            GameData gameInfo = client.getGameByID(gameNumber);
            JoinRequest request = new JoinRequest(playerColor, gameNumber);
            client.joinGame(request);

            return "Successfully joined " + gameInfo.gameName() + " as " + playerColor + "\n" + playGame(playerColor, gameInfo);
        }
        catch(Exception e){
            return "Couldn't join game";
        }


    }
    public String playGame(String playerColor, GameData gameData){
        ChessGame.TeamColor playerTeam = null;
        if(Objects.equals(playerColor, "white")){
            playerTeam = ChessGame.TeamColor.WHITE;
        }
        else{
            playerTeam = ChessGame.TeamColor.BLACK;
        }
        return new ClientUI(client.getAuth()).chessBoardDisplay(playerTeam, gameData);

    }
    public String createEval(String[] parameters){
        try{
        if(parameters.length != 1){
            return "Wrong number of arguments";
        }
        if(authorization == null){
            return "Not logged in!";
        }
        String gameName = parameters[0];
        GameData response = client.createGame(new GameData(0, null, null, gameName, null));
        return "Created game #" + response.gameID() + " named " + response.gameName();
        }catch (Exception e){
            return "";
        }
    }

    public String logoutEval(){
        try{
        if(authorization != null){
            client.logoutUser();
            authorization = client.getAuth();
            return "Logout Successful";
        }else {
            return "Not logged in";
        }
        }catch(Exception e){
            return "";
        }


    }
    public String listEval(){
        try{
        if(client.getAuth() == null){
            return "Not logged in!";
        }
        ListModel serverResponse = client.listGames();
        List<GameData> gameList = (List<GameData>) serverResponse.games();
        String consoleResponse = "";
        for(int i = 0; i < gameList.size(); i++){
            int gameNumber = i + 1;
            consoleResponse += SET_TEXT_COLOR_MAGENTA + SET_TEXT_BOLD + "Game# " + gameNumber + " ";
            consoleResponse += RESET_TEXT_BOLD_FAINT + "Game name: " + gameList.get(i).gameName() + " ";
            consoleResponse += SET_TEXT_COLOR_RED + "White username: " + gameList.get(i).whiteUsername() + " " + RESET_TEXT_COLOR;
            consoleResponse += SET_TEXT_COLOR_BLUE + "Black username: " + gameList.get(i).blackUsername() + " " + RESET_TEXT_COLOR + "\n";

        }

        return consoleResponse;}catch(Exception e){
            return "";
        }
    }

    public String helpEval(){
        return new ClientUI(authorization).helpDisplay();
    }
    public String formatResponse(Object response, Class<?> responseType) {
        if(responseType == AuthData.class){
            AuthData data = (AuthData) response;
            return "Welcome " + data.username() + "!\n" + helpEval();
        }



        return null;
    }


}
