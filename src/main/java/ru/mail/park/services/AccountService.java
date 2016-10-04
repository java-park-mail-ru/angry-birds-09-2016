package ru.mail.park.services;

import org.springframework.stereotype.Service;
import ru.mail.park.model.UserProfile;

import java.util.HashMap;
import java.util.Map;

@Service
public class AccountService {
    private Map<String, UserProfile> userNameToUser = new HashMap<>();

    public UserProfile addUser(long id, String login, String password, String email) {
        final UserProfile userProfile = new UserProfile(id, login, password, email);
        userNameToUser.put(login, userProfile);
        return userProfile;
    }

    public UserProfile getUser(String login) {
        return userNameToUser.get(login);
    }

    public UserProfile getUser(int id) {
        for (Map.Entry<String, UserProfile> entry : userNameToUser.entrySet()) {
            UserProfile user = entry.getValue();
            if (user.getId() == id) return user;
        }

        return null;
    }

    public void deleteUser(UserProfile user) {
        userNameToUser.remove(user.getLogin());
    }
}
