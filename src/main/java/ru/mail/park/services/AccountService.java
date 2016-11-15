package ru.mail.park.services;

import org.springframework.stereotype.Service;
import ru.mail.park.database.dao.UserDAO;
import ru.mail.park.database.entities.User;
import ru.mail.park.models.UserProfile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class AccountService {

    public User addUser(User user) {
        final UserDAO userDAO = new UserDAO();
        userDAO.addUser(user);
        return user;
    }

    public User getUser(String login) {
        final UserDAO userDAO = new UserDAO();
        final User user = userDAO.getUser(login);
        return user;
    }

    public User getUser(int userId) {
        final UserDAO userDAO = new UserDAO();
        final User user = userDAO.getUser(userId);
        return user;
    }

    public ArrayList<User> getAllUsers() {
        return null;
    }

    public void deleteUser(UserProfile user) {

    }
}
