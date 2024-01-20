package ru.flanker.domain.repository;

import org.hibernate.Session;
import ru.flanker.domain.model.User;

import java.util.List;

/**
 * @author 1ommy
 * @version 14.01.2024
 */
public interface UserRepository {
    void save(Session session, User user);

    List<User> findAll(Session session);


    User findOne(Session session,Integer id);
}
