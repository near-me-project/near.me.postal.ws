package services;

import javax.websocket.Session;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class UserSessionStorage {
    private static Lock reentrantLock = new ReentrantLock();

    private static UserSessionStorage instance;

    private final ConcurrentHashMap<String, Session> usersSessionMap = new ConcurrentHashMap<>();

    private UserSessionStorage() {
    }

    public static UserSessionStorage getInstance() {
        reentrantLock.lock();

        try {

            if (instance == null) {
                instance = new UserSessionStorage();
            }

        } finally {
            reentrantLock.unlock();
        }

        return instance;
    }

    public void addSession(String clientId, Session session) {
        usersSessionMap.put(clientId, session);
    }

    public Optional<Session> tryGetSession(String clientId, int timeout, TimeUnit timeUnit) {
        try {
            if (reentrantLock.tryLock(timeout, timeUnit)) {

                if (usersSessionMap.containsKey(clientId))
                    return Optional.of(usersSessionMap.get(clientId));
                else
                    return Optional.empty();

            } else return Optional.empty();
        } catch (Exception e) {
            System.out.println("[Error]: " + e.getMessage());
            return Optional.empty();
        } finally {
            reentrantLock.unlock();
        }
    }
}
