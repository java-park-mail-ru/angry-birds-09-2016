package ru.mail.park.java.service;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.junit4.SpringRunner;
import ru.mail.park.database.entities.User;
import ru.mail.park.services.AccountService;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

/**
 * Created by farid on 11.12.16.
 */
@SpringBootTest(webEnvironment = RANDOM_PORT)
@RunWith(SpringRunner.class)
public class AccountServiceTest {
    @Autowired
    AccountService accountService;

    User user1;
    User user2;

    @Before
    public void setup() {
        user1 = new User("shrek", "shrek@shrek.shrek", "kek123");
        user2 = new User("kek", "kek@kek.kek", "shrek123");

        accountService.addUser(user1);
        accountService.addUser(user2);
    }

    @After
    public void cleanUp() {
        accountService.deleteUser(user1);
        accountService.deleteUser(user2);
    }

    @Test
    public void addUserTest() {
        assertNotNull(user1.getUserId());
        assertNotNull(user2.getUserId());

        //assertNull(accountService.addUser(user1));
    }

    @Test
    public void getUserTest() {
        assertEquals(accountService.getUser(user1.getUserId()), user1);
        assertEquals(accountService.getUser(user2.getUserId()), user2);

        assertEquals(accountService.getUser("shrek"), user1);
        assertEquals(accountService.getUser("kek"), user2);
    }

    @Test
    public void updateUserTest() {
        user1.setEmail("majestic@shrek.kek");
        accountService.updateUser(user1);

        assertEquals(accountService.getUser("shrek").getEmail(), "majestic@shrek.kek");
    }

    @Test
    public void deleteAddUserTest() {
        accountService.deleteUser(user1);
        assertEquals(accountService.getUser("shrek"), null);

        accountService.addUser(user1);
        assertEquals(accountService.getUser("shrek"), user1);
    }

    @Test
    public void getAllUsersTest() {
        final ArrayList<User> list = (ArrayList<User>) accountService.getAllUsers();

        assertEquals(list.get(0), user1);
        assertEquals(list.get(1), user2);
    }
}
