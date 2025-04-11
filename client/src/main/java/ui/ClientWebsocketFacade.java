package ui;

import com.google.gson.Gson;
import websocket.commands.MakeMoveCommand;
import websocket.commands.UserGameCommand;

import javax.websocket.*;
import java.io.IOException;
import java.net.URI;

import static ui.EscapeSequences.*;
public class ClientWebsocketFacade extends Endpoint {

    Session session;
    NotificationHandler notificationHandler;
    public ClientWebsocketFacade(String url, NotificationHandler notificationHandler) throws Exception {
        try{
            url = url.replace("http", "ws");
            URI socketURI = new URI(url + "/ws");
            this.notificationHandler = notificationHandler;
            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            this.session = container.connectToServer(this, socketURI);

            this.session.addMessageHandler(new MessageHandler.Whole<String>() {
            @Override
            public void onMessage(String message){

                model.Notification notification = new Gson().fromJson(message, model.Notification.class);
                notificationHandler.notify(notification);


            }
        });
        }catch (Exception e){
            System.out.println("Error in Client Websocket Facade" + e.getMessage());
        }
    }


    public void connect(UserGameCommand userGameCommand) throws IOException {
        System.out.println(SET_TEXT_COLOR_WHITE + "Connecting to game..." + RESET_TEXT_COLOR);
        try{
            this.session.getBasicRemote().sendText(new Gson().toJson(userGameCommand));}
        catch(Exception e){
            System.out.println(SET_TEXT_COLOR_RED + "Failed to connect to Websocket -> " + e.getMessage() + RESET_TEXT_COLOR);
        }

    }

    public void makeMove(MakeMoveCommand makeMove){
        System.out.println(SET_TEXT_COLOR_LIGHT_GREY + "Moving " +
                makeMove.getMove().getStartPosition().toString() + " to " + makeMove.getMove().getEndPosition().toString());
        try{
            this.session.getBasicRemote().sendText(new Gson().toJson(makeMove));
        }catch(Exception e){
            System.out.println("Error trying to make move");
        }
    }

    public void onOpen(Session session, EndpointConfig endpointConfig) {
    }
}