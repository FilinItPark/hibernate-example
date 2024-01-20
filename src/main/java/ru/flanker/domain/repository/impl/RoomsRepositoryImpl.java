package ru.flanker.domain.repository.impl;

import jakarta.persistence.criteria.CriteriaBuilder;
import org.hibernate.Session;
import ru.flanker.domain.model.Rooms;
import ru.flanker.domain.model.User;
import ru.flanker.domain.repository.RoomsRepository;

import java.util.List;

/**
 * @author 1ommy
 * @version 14.01.2024
 */
public class RoomsRepositoryImpl implements RoomsRepository {
    @Override
    public void save(Session session, Rooms room) {

        session.beginTransaction();
        session.persist(room);
        session.getTransaction().commit();
    }

    @Override
    public List<Rooms> findAll(Session session) {
        List<Rooms> rooms;

        session.beginTransaction();

        CriteriaBuilder cb = session.getCriteriaBuilder();

        var criteriaQuery = cb.createQuery(Rooms.class);

        var baseClass = criteriaQuery.from(Rooms.class);

        criteriaQuery.select(baseClass);

        var query = session.createQuery(criteriaQuery);

        rooms = query.getResultList();

        session.getTransaction().commit();


        return rooms;
    }

    @Override
    public Rooms findOne(Session session, Integer id) {
        Rooms room;

        session.beginTransaction();

        room = session.get(Rooms.class, id);

        session.getTransaction().commit();

        return room;
    }

    @Override
    public void bookRoom(Session session, Integer roomId, User bookedBy) {
        Rooms room = findOne(session, roomId);

        session.beginTransaction();

        room.setBookedBy(bookedBy);

        session.persist(room);
        session.getTransaction().commit();

    }
}
