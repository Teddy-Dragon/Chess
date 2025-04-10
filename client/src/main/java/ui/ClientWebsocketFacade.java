package ui;

import com.google.gson.Gson;
import org.eclipse.jetty.io.EndPoint;

import javax.management.Notification;
import javax.websocket.*;
import java.net.URI;


public class ClientWebsocketFacade extends EndPoint {
    Session session;
    NotificationHandler notificationHandler;

    public ClientWebsocketFacade(String url, NotificationHandler notificationHandler){
        try{
            url = url.replace("http", "ws");
            URI socketURI = new URI(url + "/ws");
            this.notificationHandler = notificationHandler;
            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            this.session = container.connectToServer(this, socketURI);
            this.session.addMessageHandler(new MessageHandler.Whole<String>(){
                @Override
                public void onMessage(String message){
                    Notification notification = new Gson().fromJson(message, Notification.class);
                    notificationHandler.notify(notification);
                }
            });

        }catch(Exception e){
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void onOpen(Session session, EndpointConfig endpointConfig) {

    }
}
