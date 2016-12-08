package ru.mail.park.database.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;
import ru.mail.park.database.entities.User;

/**
 * Created by farid on 14.11.16.
 */
@SuppressWarnings("InterfaceNeverImplemented")
@Transactional
public interface UserDAO extends JpaRepository<User, Long> {

    User findByLogin(String login);

    User findByUserId(Integer userId);
}