package services;

import javax.websocket.Session;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.*;

public class UserSessionStorage {
    private static Lock reentrantLock = new ReentrantLock();
    private static UserSessionStorage instance;

    private ReadWriteLock readWriteLock;
    private Lock readLock;
    private Lock writeLock;


    private final ConcurrentHashMap<String, Session> usersSessionMap = new ConcurrentHashMap<>();

    private UserSessionStorage() {
        boolean fair = true;
        readWriteLock = new ReentrantReadWriteLock(fair);
        readLock = readWriteLock.readLock();
        writeLock = readWriteLock.writeLock();
    }

    public static UserSessionStorage getInstance() {
        reentrantLock.lock();

        try {
            if (instance == null) instance = new UserSessionStorage();
        } finally {
            reentrantLock.unlock();
        }

        return instance;
    }

    public void addSession(String clientId, Session session) {
        writeLock.lock();
        try {
            usersSessionMap.put(clientId, session);
        } finally {
            writeLock.unlock();
        }
    }

    public Optional<Session> tryGetSession(String clientId, int timeout, TimeUnit timeUnit) {
        try {
            if (readLock.tryLock(timeout, timeUnit)) {

                if (usersSessionMap.containsKey(clientId))
                    return Optional.of(usersSessionMap.get(clientId));
                else
                    return Optional.empty();

            } else return Optional.empty();
        } catch (Exception e) {
            System.out.println("[Error]: " + e.getMessage());
            return Optional.empty();
        } finally {
            readLock.unlock();
        }
    }
}
