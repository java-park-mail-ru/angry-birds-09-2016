package ru.mail.park.main.game.mechanics;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import ru.mail.park.main.game.gamesession.LobbySessionService;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Created by farid on 15.12.16.
 */
@Service
public class MechanicsExecutor implements Runnable {
    @Autowired
    private LobbySessionService lobby;

    private int position = 0;
    private int end = 1000;
    private int begin = 0;
    private int direction = 1;

    private Executor tickExecutor = Executors.newSingleThreadExecutor();

    @PostConstruct
    public void initAfterStartup() {
        tickExecutor.execute(this);
    }

    @Override
    public void run() {
        while (true) {
            try {
                for (WebSocketSession session : lobby.getSessions()) {
                    session.sendMessage(new TextMessage("{\"position\":" + position + '}'));
                }
                Thread.sleep(20);

                if (position > end) direction = -1;
                if (position < begin) direction = 1;

                position += 3 * direction;
            } catch (InterruptedException|IOException ex) {
                ex.printStackTrace();
            }
        }
    }
}
