package ru.flanker.domain.repository;

import org.hibernate.Session;
import ru.flanker.domain.model.Rooms;
import ru.flanker.domain.model.User;

import java.util.List;

/**
 * @author 1ommy
 * @version 14.01.2024
 */
public interface RoomsRepository {
    void save(Session session, Rooms room);

    List<Rooms> findAll(Session session);

    Rooms findOne(Session session, Integer id);

    void bookRoom(Session session, Integer roomId, User bookedBy);
}
