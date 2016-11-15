package ru.mail.park.services;

import org.springframework.stereotype.Service;
import ru.mail.park.database.entities.User;
import ru.mail.park.models.UserProfile;

import java.util.HashMap;
import java.util.Map;

@Service
public class SessionService {
    private Map<String, User>  sessionIdToUser = new HashMap<>();

    public void addUserToSession (String sessionId, User user) {
        sessionIdToUser.put(sessionId, user);
    }

    public void endSession (String sessionId) {
        sessionIdToUser.remove(sessionId);
    }

    public User getUserBySessionId(String sessionId) {
        return sessionIdToUser.get(sessionId);
    }

    public long getLength() {
        return sessionIdToUser.size();
    }
}