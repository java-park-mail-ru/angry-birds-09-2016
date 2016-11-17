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
@SuppressWarnings({"JpaQlInspection", "unchecked"})
public class UserDAO {

    public <T> User getUser(T parameter, String parameterName) {
        final Session session = HibernateUtils.getSessionFactory().openSession();
        final Transaction transaction = session.beginTransaction();

        final Query query = session.createQuery("from User user where user." + parameterName + " = :" + parameterName);

        final User user = (User) query.setParameter(parameterName, parameter).uniqueResult();

        transaction.commit();
        session.close();

        return user;
    }

    public void addUser(User user) {
        final Session session = HibernateUtils.getSessionFactory().openSession();
        final Transaction transaction = session.beginTransaction();

        user.setUserId((Integer) session.save(user));

        transaction.commit();
        session.close();
    }

    public List<User> getUserList() {
        final Session session = HibernateUtils.getSessionFactory().openSession();
        final Transaction transaction = session.beginTransaction();

        final List users = session.createQuery("from User user").list();

        transaction.commit();
        session.close();

        return users;
    }

    public void deleteUser(User user) {
        final Session session = HibernateUtils.getSessionFactory().openSession();
        final Transaction transaction = session.beginTransaction();

        session.delete(user);

        transaction.commit();
        session.close();
    }
}