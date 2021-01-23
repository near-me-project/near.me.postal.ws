package controller;


import messagedecoders.LocationsModelEncoder;
import messagedecoders.SubscriptionRequestModelDecoder;
import model.SubscriptionRequestModel;
import services.MessageBrokerService;
import services.UserSessionStorage;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

@ServerEndpoint(value = "/subscribe/{clientId}",
        decoders = SubscriptionRequestModelDecoder.class,
        encoders = LocationsModelEncoder.class)
public class SubscribeController {

    private UserSessionStorage userSessionStorage;

    public SubscribeController() {
        new MessageBrokerService().declareInfoServiceListener();
        userSessionStorage = UserSessionStorage.getInstance();
    }

    @OnOpen
    public void onOpen(Session session, @PathParam("clientId") String clientId) {
        System.out.println("Opening session: [" + session.getId() + "] for client: [" + clientId + "]");
        userSessionStorage.addSession(clientId, session);
    }

    @OnMessage
    public void onMessage(Session session, SubscriptionRequestModel subscriptionRequestModel) {
        System.out.println("Message received ...Unexpected");
    }

    @OnClose
    public void onClose(Session session) {
        System.out.println("Closing session: [" + session.getId() + "]");
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        System.out.println("[Error]: for session: [" + session.getId() + "]");
        System.out.println(throwable.getMessage());
    }
}
