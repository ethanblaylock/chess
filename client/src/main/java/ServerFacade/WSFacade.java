package ServerFacade;

import com.google.gson.Gson;
import dataAccess.DataAccessException;
import webSocketMessages.serverMessages.Notification;
import webSocketMessages.serverMessages.ServerMessage;

import javax.websocket.*;

import java.net.URI;


public class WSFacade extends Endpoint {

    public Session session;
    public WSFacade() throws Exception {
        URI uri = new URI("ws://localhost:8000/connect");
        WebSocketContainer container = ContainerProvider.getWebSocketContainer();
        this.session = container.connectToServer(this, uri);

        this.session.addMessageHandler(new MessageHandler.Whole<String>() {

            public void onMessage(String message) {
                ServerMessage tempMessage = new Gson().fromJson(message, ServerMessage.class);
                switch (tempMessage.getServerMessageType()) {
                    case ERROR:
                        System.out.println(new Gson().fromJson(message, Error.class).getMessage());
                        break;
                    case NOTIFICATION:
                        System.out.println(new Gson().fromJson(message, Notification.class).getMessage());
                        break;
                    case LOAD_GAME:
                        try {
                            ServerFacade.redrawBoard();
                        } catch (DataAccessException e) {
                            throw new RuntimeException(e);
                        }
                        break;
                }
            }
        });
    }

    public void send(String command) throws Exception {
        this.session.getBasicRemote().sendText(command);

    }

    public void onOpen(Session session, EndpointConfig endpointConfig) {
        this.session = session;
    }

}
