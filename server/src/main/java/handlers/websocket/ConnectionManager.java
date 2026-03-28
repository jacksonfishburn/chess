package handlers.websocket;

import json.JsonSerializer;
import org.eclipse.jetty.websocket.api.Session;
import websocket.messages.ServerMessage;

import java.io.IOException;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class ConnectionManager {
    public final ConcurrentHashMap<Integer, Set<Session>> connections = new ConcurrentHashMap<>();

    public void add(Session session, int gameID) {
        connections.computeIfAbsent(gameID, k -> ConcurrentHashMap.newKeySet()).add(session);
    }

    public void remove(Session session, int gameID) {
        if (connections.containsKey(gameID)) {
            connections.get(gameID).remove(session);
        }
    }

    public void broadcast(Session excludeSession, int gameID, ServerMessage message) throws IOException {
        String msg = JsonSerializer.toJson(message);
        Set<Session> players = connections.getOrDefault(gameID, Set.of());
        for (Session session : players) {
            if (session.isOpen()) {
                if (!session.equals(excludeSession)) {
                    session.getRemote().sendString(msg);
                }
            }
        }
    }
}