package ru.flanker.application.service;

import org.hibernate.Session;
import ru.flanker.domain.model.Rooms;
import ru.flanker.domain.model.User;

import java.util.List;

/**
 * @author 1ommy
 * @version 20.01.2024
 */
public interface RoomsService {

    void saveRoom(Session session, Rooms room);

    List<Rooms> getAllRooms(Session session);

    Rooms getRoomById(Session session, Integer id);

    void bookARoom(Session session, Integer roomId, User user);
}