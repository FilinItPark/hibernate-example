package ru.flanker.domain.repository.impl;

import jakarta.persistence.criteria.CriteriaBuilder;
import org.hibernate.Session;
import org.hibernate.query.Query;
import ru.flanker.domain.model.User;
import ru.flanker.domain.repository.UserRepository;

import java.util.List;

/**
 * @author 1ommy
 * @version 14.01.2024
 */
public class UserRepositoryImpl implements UserRepository {


    @Override
    public void save(Session session, User user) {
        session.beginTransaction();
        session.persist(user);
        session.getTransaction().commit();
    }

    @Override
    public List<User> findAll(Session session) {
        List<User> users;

        session.beginTransaction();

        CriteriaBuilder cb = session.getCriteriaBuilder();

        var criteriaQuery = cb.createQuery(User.class);

        var baseClass = criteriaQuery.from(User.class);

        criteriaQuery.select(baseClass);

        Query<User> query = session.createQuery(criteriaQuery);

        users = query.getResultList();

        session.getTransaction().commit();

        return users;
    }

    @Override
    public User findOne(Session session, Integer id) {
        User user;

        session.beginTransaction();

        user = session.get(User.class, id);

        session.getTransaction().commit();

        return user;
    }
}
