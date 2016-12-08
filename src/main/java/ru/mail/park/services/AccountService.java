package ru.mail.park.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.mail.park.database.dao.UserDAO;
import ru.mail.park.database.entities.User;

@Service
public class AccountService {

    @Autowired
    private UserDAO userDAO;

    public User addUser(User user) {
        return userDAO.save(user);
    }

    public User getUser(String login) {
        return userDAO.findByLogin(login);
    }

    public User getUser(int userId) {
        return userDAO.findByUserId(userId);
    }

    public Iterable<User> getAllUsers() {
        return userDAO.findAll();
    }

    public void deleteUser(User user) {
        userDAO.delete(user);
    }
}
