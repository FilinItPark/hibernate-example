package ru.flanker.application.service.impl;

/**
 * @author 1ommy
 * @version 20.01.2024
 */

import org.hibernate.Session;
import ru.flanker.application.service.RoomsService;
import ru.flanker.domain.model.Rooms;
import ru.flanker.domain.model.User;
import ru.flanker.domain.repository.RoomsRepository;
import ru.flanker.domain.repository.impl.RoomsRepositoryImpl;

import java.util.List;

public class RoomsServiceImpl implements RoomsService {

    private final RoomsRepository roomsRepository;

    public RoomsServiceImpl(RoomsRepository roomsRepository) {
        this.roomsRepository = roomsRepository;
    }

    public void saveRoom(Session session, Rooms room) {
        roomsRepository.save(session, room);
    }

    public List<Rooms> getAllRooms(Session session) {
//        return List.of(new Rooms(), new Rooms());
        return roomsRepository.findAll(session);
    }

    public Rooms getRoomById(Session session, Integer id) {
        return roomsRepository.findOne(session, id);
    }

    public void bookARoom(Session session, Integer roomId, User user) {
        roomsRepository.bookRoom(session, roomId, user);
    }
}