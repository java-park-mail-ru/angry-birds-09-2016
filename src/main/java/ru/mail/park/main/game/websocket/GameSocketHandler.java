package ru.mail.park.main.game.websocket;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import ru.mail.park.main.game.gamesession.LobbySessionService;
import ru.mail.park.services.AccountService;

import javax.naming.AuthenticationException;
import javax.validation.constraints.NotNull;
import java.io.IOException;

/**
 * Created by farid on 13.12.16.
 */
public class GameSocketHandler extends TextWebSocketHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(GameSocketHandler.class);

    private final ObjectMapper mapper = new ObjectMapper();

    @Autowired
    private LobbySessionService lobby;

    @Override
    public void afterConnectionEstablished(WebSocketSession webSocketSession) throws AuthenticationException {
        lobby.getSessions().add(webSocketSession);

        try {
            for (WebSocketSession session : lobby.getSessions()) {
                session.sendMessage(new TextMessage("Someone has entered the lobby " + webSocketSession.toString()));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws AuthenticationException {
        try {
            session.sendMessage(new TextMessage("Hi yourself"));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }


    @Override
    public void handleTransportError(WebSocketSession webSocketSession, Throwable throwable) throws Exception {
        LOGGER.warn("Websocket transport problem", throwable);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession webSocketSession, CloseStatus closeStatus) throws Exception {
            lobby.getSessions().remove(webSocketSession);
    }

    @Override
    public boolean supportsPartialMessages() {
        return false;
    }
}
