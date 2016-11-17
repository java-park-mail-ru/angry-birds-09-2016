package ru.mail.park.java.database;

import org.hibernate.Session;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;
import ru.mail.park.database.HibernateUtils;
import ru.mail.park.database.dao.UserDAO;
import ru.mail.park.database.entities.User;

/**
 * Created by farid on 17.11.16.
 */
@RunWith(SpringRunner.class)
public class DatabaseTest {

    @Test
    public void insertionTest() {
        UserDAO userDAO = new UserDAO();
        User user = new User("shrek", "shrek@kek.kek", "kek");

        userDAO.addUser(user);
        Assert.assertNotNull("", userDAO.getUser("shrek", "login"));
        
        userDAO.deleteUser(user);
    }

    @Test
    public void deletionTest() {
        UserDAO userDAO = new UserDAO();
        User user = new User("shrek", "shrek@kek.kek", "kek");

        userDAO.addUser(user);

        userDAO.deleteUser(user);
        Assert.assertNull("", userDAO.getUser("shrek", "login"));
    }
}
