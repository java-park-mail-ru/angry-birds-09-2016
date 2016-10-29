package ru.mail.park.services;

import org.springframework.stereotype.Service;
import ru.mail.park.model.UserProfile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class AccountService {
    private Map<String, UserProfile> userNameToUser = new HashMap<>();
    private static final AtomicLong ID_GENETATOR = new AtomicLong(0);

    public UserProfile addUser(String login, String password, String email) {
        final long id = ID_GENETATOR.getAndIncrement();
        final UserProfile userProfile = new UserProfile(id, login, password, email);
        userNameToUser.put(login, userProfile);
        return userProfile;
    }

    public UserProfile getUser(String login) {
        return userNameToUser.get(login);
    }

    public UserProfile getUser(int id) {
        for (Map.Entry<String, UserProfile> entry : userNameToUser.entrySet()) {
            final UserProfile user = entry.getValue();
            if (user.getId() == id) return user;
        }

        return null;
    }

    public ArrayList<UserProfile> getAllUsers() {
        final ArrayList<UserProfile> userList = new ArrayList<>();
        for (Map.Entry<String, UserProfile> entry : userNameToUser.entrySet()) {
            userList.add(entry.getValue());
        }
        return userList;
    }

    public void deleteUser(UserProfile user) {
        userNameToUser.remove(user.getLogin());
    }
}
