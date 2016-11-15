package ru.mail.park.services;

import org.springframework.stereotype.Service;
import ru.mail.park.database.dao.UserDAO;
import ru.mail.park.database.entities.User;

import java.util.List;

@Service
public class AccountService {

    public User addUser(User user) {
        final UserDAO userDAO = new UserDAO();
        userDAO.addUser(user);
        return user;
    }

    public User getUser(String login) {
        final UserDAO userDAO = new UserDAO();
        return userDAO.getUser(login, "login");
    }

    public User getUser(int userId) {
        final UserDAO userDAO = new UserDAO();
        return userDAO.getUser(userId, "userId");
    }

    public List<User> getAllUsers() {
        final UserDAO userDAO = new UserDAO();
        return userDAO.getUserList();
    }

    public void deleteUser(User user) {
        final UserDAO userDAO = new UserDAO();
        userDAO.deleteUser(user);
    }
}
