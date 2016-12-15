package ru.mail.park.main.game.gamesession;

import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketSession;

import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by farid on 13.12.16.
 */
@Service
public class LobbySessionService {

    private ConcurrentLinkedQueue<WebSocketSession> sessions;

    LobbySessionService() {
        sessions = new ConcurrentLinkedQueue<>();
    }

    public ConcurrentLinkedQueue<WebSocketSession> getSessions() {
        return sessions;
    }

    public void setSessions(ConcurrentLinkedQueue<WebSocketSession> sessions) {
        this.sessions = sessions;
    }
}
