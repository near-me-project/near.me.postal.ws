package services;

import com.google.gson.Gson;
import model.LocationsModel;

import javax.websocket.EncodeException;
import javax.websocket.Session;
import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

public class ClientNotificatorService {

    private UserSessionStorage userSessionStorage;

    public ClientNotificatorService() {
        userSessionStorage = UserSessionStorage.getInstance();
    }

    public CompletableFuture<Boolean> notifyClientWithMessage(byte[] event) {

        System.out.println("Income event received: " + new String(event));

        return CompletableFuture.supplyAsync(() -> sendNotification(event));
    }

    private Boolean sendNotification(byte[] event) {
        LocationsModel locationsModel = new Gson().fromJson(new String(event), LocationsModel.class);
        return userSessionStorage
                .tryGetSession(locationsModel.getClientId(), 100, TimeUnit.MILLISECONDS)
                .map(session -> send(locationsModel, session))
                .orElse(false);
    }

    private Boolean send(LocationsModel locationsModel, Session session) {
        try {
            session.getBasicRemote().sendObject(locationsModel);
            return true;
        } catch (IOException | EncodeException e) {
            e.printStackTrace();
            return false;
        }
    }
}
