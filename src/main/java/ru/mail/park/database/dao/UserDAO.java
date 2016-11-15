package ru.mail.park.database.dao;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import ru.mail.park.database.HibernateUtils;
import ru.mail.park.database.entities.User;

import java.util.List;

/**
 * Created by farid on 14.11.16.
 */
@SuppressWarnings("JpaQlInspection")
public class UserDAO {
    public <T> User getUser(T parameter) {
        final Session session = HibernateUtils.getSessionFactory().openSession();
        final Transaction transaction = session.beginTransaction();

        final Query query = session.createQuery("from User user where user.login = :login");

        transaction.commit();
        session.close();

        return (User) query.setParameter("login", parameter).uniqueResult();
    }

    public void addUser(User user) {
        final Session session = HibernateUtils.getSessionFactory().openSession();
        final Transaction transaction = session.beginTransaction();

        user.setUserId((Integer) session.save(user));

        transaction.commit();
        session.close();
    }
}