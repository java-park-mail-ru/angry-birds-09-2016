package ru.mail.park.services;

import org.springframework.stereotype.Service;
import ru.mail.park.model.UserProfile;

import java.util.HashMap;
import java.util.Map;

@Service
public class SessionService {
    private Map<String, UserProfile>  sessionIdToUser = new HashMap<>();

    public void addUserToSession (String sessionId, UserProfile userProfile) {
        sessionIdToUser.put(sessionId, userProfile);
    }

    public void endSession (String sessionId) {
        sessionIdToUser.remove(sessionId);
    }

    public UserProfile getUserBySessionId(String sessionId) {
        return sessionIdToUser.get(sessionId);
    }

    public long getLength() {
        return sessionIdToUser.size();
    }
}