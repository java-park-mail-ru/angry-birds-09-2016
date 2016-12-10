package ru.mail.park.database.dao;

import org.hibernate.HibernateException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;
import ru.mail.park.database.entities.User;

import java.util.List;

/**
 * Created by farid on 14.11.16.
 */
public interface UserDao {
    User save(User user) throws DataIntegrityViolationException;

    User findByLogin(String login);

    User findByUserId(Integer userId);

    List<User> findAll();

    void update(User user) throws DataIntegrityViolationException;

    void delete(User user);
}