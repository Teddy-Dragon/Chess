package ui;

import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPiece;
import chess.ChessPosition;
import model.*;
import websocket.commands.MakeMoveCommand;
import websocket.commands.UserGameCommand;

import java.io.IOException;
import java.util.*;

import static ui.EscapeSequences.*;

public class ChessClient {
    private UUID authorization;
    private String username;
    private String serverURL;
    private ClientServerFacade client;
    private ClientWebsocketFacade websocketClient;
    private NotificationHandler notificationHandler;
    private ChessGame.TeamColor chessBoardPerspective;
    public ChessClient(String serverUrl, NotificationHandler notificationHandler){
        this.client = new ClientServerFacade(serverUrl);
        this.notificationHandler = notificationHandler;
        this.serverURL = serverUrl;

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
            readTeamColor(playerColor);
            try{
                websocketClient = new ClientWebsocketFacade(serverURL, notificationHandler);
            }catch(Exception ex){
                System.out.println("Websocket Error");
            }
                GameData gameData = client.getGameByID(gameID);
                return inGame(new JoinRequest(playerColor, gameID), false);
        }catch(Exception e){
            return "";
        }

    }
    public String makeMove(String[] parameters, int gameID){
        if(parameters.length < 2 || parameters.length > 3){
            return "Incorrect number of arguements";
        }
        if(!parameters[0].matches("[A-z][0-8]") && !parameters[0].matches("[0-8][A-z]")){
            return "Invalid start position";
        }
        if(!parameters[1].matches("[A-z][0-8]") && !parameters[1].matches("[0-8][A-z]")){
            return "Invalid end position";
        }
        var startLetter = parameters[0].split("");
        int col = 0;
        int row = 0;
        if(readChessMove(startLetter[0]) == 1000){
            row = Integer.parseInt(startLetter[0]);
        }
        else{
            col = readChessMove(startLetter[0]) + 1;
        }
        if(readChessMove(startLetter[1]) == 1000){
            row = Integer.parseInt(startLetter[1]);
        }
        else{
            col = readChessMove(startLetter[1]);
        }
        ChessPosition startPosition = new ChessPosition(row, col);
        var endLetter = parameters[1].split("");
        if(readChessMove(endLetter[0]) == 1000){
            row = Integer.parseInt(endLetter[0]);
        }
        else{
            col = readChessMove(endLetter[0]) + 1;
        }
        if(readChessMove(endLetter[1]) == 1000){
            row = Integer.parseInt(endLetter[1]);
        }
        else{
            col = readChessMove(endLetter[1]);
        }
        ChessPosition endPosition = new ChessPosition(row, col);
        ChessMove move;
        if(parameters.length == 2) {
            move = new ChessMove(startPosition, endPosition, null);
        }
        else{
            ChessPiece.PieceType pieceType = validatePromotion(parameters[2]);
            if(pieceType == null){
                return "Invalid promotion piece";
            }
            else{
                move = new ChessMove(startPosition, endPosition, pieceType);
            }
        }
        try{
            websocketClient.makeMove(new MakeMoveCommand(MakeMoveCommand.CommandType.MAKE_MOVE, client.getAuth().toString(), gameID, move));}
        catch (Exception e){
            System.out.println("Problem with making a move");
        }
        return "";
    }
    public String highlightValid(String[] parameters, JoinRequest request, Boolean player){
        if(parameters.length != 1){
            return "Incorrect number of arguments";
        }
        var positions = parameters[0].split("");
        if(positions.length != 2){
            return "Invalid start position";
        }
        //Make sure that we have the right number of arguments and that we are starting off with a letter first
        int column = 0;
        List<String> boardLetters = Arrays.asList("a", "b", "c", "d", "e", "f", "g", "h");
        for(int i = 0; i < boardLetters.size(); i++){
            if(Objects.equals(positions[0], boardLetters.get(i))){
                column = i;

                break;
            }
            else{
                column = 10;
            }
        }
        if(!positions[1].matches("[0-8]")){
            return "Invalid start position";
        }
        int row = Integer.parseInt(positions[1]);
        if(column > 8 || row > 8 || row < 1 ){
            return "Not a valid position";
        }
        ChessPosition startPos = new ChessPosition(row, column + 1);

        try{
            GameData gameData = client.getGameByID(request.gameID());
            ChessGame game = gameData.game();
            List<ChessPosition> endPositions = new ArrayList<>();
            List<ChessMove> validMoves = (List<ChessMove>) game.validMoves(startPos);
            for(int i = 0; i < validMoves.size(); i++){
                endPositions.add(validMoves.get(i).getEndPosition());
            }
            return getGame(request.playerColor(), gameData, endPositions);


        }
        catch(Exception e){
            return "No piece is at that spot, try again!";
        }
    }

    public String inGameHelp(Boolean player){
        String line = "" + SET_TEXT_COLOR_MAGENTA;

        if(player){
            line += "Type 'move' and then <start location> <end location> <promotion if applicable> to make a move \n";
            line += "Type 'resign' to forfeit and end the game \n";
        }
        line += "Type 'redraw' to redraw the current chessboard \n";
        line += "Type 'highlight' to highlight valid moves \n";
        line += "Type 'leave' to leave the current game \n" + RESET_TEXT_COLOR;
        return line;
    }

    public String inGameEval(String input, JoinRequest request, Boolean player){
        var tokens = input.toLowerCase().split(" ");
        var parameters = Arrays.copyOfRange(tokens, 1, tokens.length);
        if(tokens.length < 1){
            return "Invalid argument length";
        }
        switch (tokens[0]){
            case "redraw" -> {
                try{
                    return getGame(request.playerColor(), client.getGameByID(request.gameID()), null);
                }catch (Exception e){
                    return "There was an issue";
                }
            }
            case "highlight" -> {
                return highlightValid(parameters, request, player);
            }
            case "move" -> {
                if(!player){
                    return inGameHelp(player);
                }
                else{
                    return makeMove(parameters, request.gameID());
                }
            }
            case "leave" -> {
                return "leave";
            }
            case "resign" -> {
                if(!player){
                    return inGameHelp(player);
                }else{
                    System.out.println("Are you sure?");
                    Scanner scanner = new Scanner(System.in);
                    String line = scanner.nextLine();
                    if(line.equalsIgnoreCase("yes")){
                        return "resign";
                    }
                    else{
                        return "";
                    }
                }
            }
            default -> {
                return inGameHelp(player);
            }
        }
    }


    public String inGame(JoinRequest request, Boolean player){
        Scanner scanner = new Scanner(System.in);
        var input = "";
        try{
            websocketClient.connect(new UserGameCommand(UserGameCommand.CommandType.CONNECT, client.
                    getAuth().toString(), request.gameID()));
            if(player){
                System.out.println(SET_TEXT_COLOR_MAGENTA + "Successfully joined game as " + request.playerColor()+ "\n");
            }
            else{
                System.out.println(SET_TEXT_COLOR_MAGENTA + "Watching as " + request.playerColor());
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        while(!input.equals("leave") && !Objects.equals(input, "resign")){
            String line = scanner.nextLine();
            input = inGameEval(line, request, player);
            if(Objects.equals(input, "leave") || Objects.equals(input, "resign")){
                break;
            }
            System.out.println(input);
        }
        if(input.equals("leave")){
            try{
                UserGameCommand userGameCommand = new UserGameCommand(UserGameCommand.CommandType.LEAVE, client.getAuth().toString(), request.gameID());
                websocketClient.disconnect(userGameCommand);
                websocketClient = null;
                return SET_TEXT_COLOR_MAGENTA + "Left Game" + RESET_TEXT_COLOR + "\n" + helpEval();
            } catch(Exception e){
                return SET_TEXT_COLOR_RED + "Something went wrong" + RESET_TEXT_COLOR;
            }
        }
        if(Objects.equals(input, "resign")) {
            websocketClient.disconnect(new UserGameCommand(UserGameCommand.CommandType.RESIGN, client.getAuth().toString(), request.gameID()));
            websocketClient = null;
            return SET_TEXT_COLOR_MAGENTA + "Resigned" + RESET_TEXT_COLOR + "\n" + helpEval();
        }
        return null;
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
            readTeamColor(playerColor);
            GameData gameInfo = client.getGameByID(gameNumber);
            JoinRequest request = new JoinRequest(playerColor, gameNumber);
            client.joinGame(request);
            try{
                websocketClient = new ClientWebsocketFacade(serverURL, notificationHandler);
            }catch(Exception ex){
                System.out.println("Websocket Error");
            }
            return inGame(request, true);
        }
        catch(Exception e){
            return "Error with Game";
        }

    }
    public String getGame(String playerColor, GameData gameData, List<ChessPosition> highlights){
        ChessGame.TeamColor playerTeam = null;
        if(Objects.equals(playerColor, "white")){
            playerTeam = ChessGame.TeamColor.WHITE;
        }
        else{
            playerTeam = ChessGame.TeamColor.BLACK;
        }
        return new ChessBoardDisplay().chessBoardDisplay(playerTeam, gameData.game(), highlights);
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
        return new HelpDisplay(authorization).helpDisplay();
    }
    public String formatResponse(Object response, Class<?> responseType) {
        if(responseType == AuthData.class){
            AuthData data = (AuthData) response;
            return "Welcome " + data.username() + "!\n" + helpEval();
        }



        return null;
    }
    public int readChessMove(String letter){
        List<String> boardLetters = Arrays.asList("a", "b", "c", "d", "e", "f", "g", "h");
        if(letter.matches("[A-z]")){
            for(int i = 0; i < boardLetters.size(); i++){
                if(letter.equalsIgnoreCase(boardLetters.get(i))){
                    return i;
                }
            }

        }
        return 1000;
    }
    private ChessPiece.PieceType validatePromotion(String promotionPiece){
        if(promotionPiece.equalsIgnoreCase("queen") || promotionPiece.equalsIgnoreCase("q")) {
            return ChessPiece.PieceType.QUEEN;
        }
        if(promotionPiece.equalsIgnoreCase("rook") || promotionPiece.equalsIgnoreCase("r")){
            return ChessPiece.PieceType.ROOK;
        }
        if(promotionPiece.equalsIgnoreCase("knight") || promotionPiece.equalsIgnoreCase("n")){
            return ChessPiece.PieceType.KNIGHT;
        }
        if(promotionPiece.equalsIgnoreCase("bishop") || promotionPiece.equalsIgnoreCase("b")){
            return ChessPiece.PieceType.BISHOP;
        }
        return null;

    }
    public String printChessGame(ChessGame game){
        return new ChessBoardDisplay().chessBoardDisplay(chessBoardPerspective, game, null);
    }
    private void readTeamColor(String playerColor){
        if(playerColor.equalsIgnoreCase("white")){
            chessBoardPerspective = ChessGame.TeamColor.WHITE;
        }
        if(playerColor.equalsIgnoreCase("black")){
            chessBoardPerspective = ChessGame.TeamColor.BLACK;
        }
    }
}