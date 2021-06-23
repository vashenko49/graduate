package com.streaming.main;

import org.springframework.messaging.simp.stomp.StompSession;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class Connections {
    private static Connections istance = null;

    Map<String, StompSession> map = new ConcurrentHashMap<>();

    private Connections() {
    }

    public static synchronized Connections getSingleton() {
        if (istance == null)
            istance = new Connections();
        return istance;
    }

    public void saveSocketConnection(StompSession session, String sessionId) { // not static, otherwise there's no need for the singleton
        map.put(sessionId, session);
    }

    public void closeConnection(String sessionId) {
        final StompSession stompSession = map.get(sessionId);
        stompSession.disconnect();
    }
}