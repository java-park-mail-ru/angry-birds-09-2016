package ru.mail.park.services;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 * Created by farid on 10.12.16.
 */
@Service
public class HibernateSessionService {
    private SessionFactory sessionFactory;

    public HibernateSessionService() {
        sessionFactory = new Configuration().configure().buildSessionFactory();
    }

    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }
}
