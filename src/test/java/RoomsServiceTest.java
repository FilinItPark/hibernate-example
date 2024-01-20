import org.hibernate.Session;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ru.flanker.application.service.RoomsService;
import ru.flanker.application.service.impl.RoomsServiceImpl;
import ru.flanker.domain.model.Rooms;
import ru.flanker.domain.model.User;
import ru.flanker.domain.repository.RoomsRepository;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class RoomsServiceTest {

    @Mock
    private RoomsRepository roomsRepository;

    @Mock
    private Session session;

    private RoomsService roomsService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        roomsService = new RoomsServiceImpl(roomsRepository);
    }

    @Test
    void getAllRooms_ShouldReturnListOfRooms() {
        // Arrange
        List<Rooms> expectedRooms = List.of(new Rooms(), new Rooms());
        when(roomsRepository.findAll(session)).thenReturn(expectedRooms);

        // Act
        List<Rooms> actualRooms = roomsService.getAllRooms(session);

        // Assert
        assertEquals(expectedRooms, actualRooms);
        verify(roomsRepository).findAll(session);
    }

    // Additional tests can be added below
    @Test
    void saveRoom_ShouldInvokeSaveOnRepository() {
        // Arrange
        Rooms room = new Rooms();
        doNothing().when(roomsRepository).save(session, room);

        // Act
        roomsService.saveRoom(session, room);

        // Assert
        verify(roomsRepository).save(session, room);
    }

    @Test
    void getRoomById_ShouldReturnRoom() {
        // Arrange
        Integer roomId = 1;
        Rooms expectedRoom = new Rooms();
        when(roomsRepository.findOne(session, roomId)).thenReturn(expectedRoom);

        // Act
        Rooms actualRoom = roomsService.getRoomById(session, roomId);

        // Assert
        assertEquals(expectedRoom, actualRoom);
        verify(roomsRepository).findOne(session, roomId);
    }

    @Test
    void bookARoom_ShouldInvokeBookRoomOnRepository() {
        // Arrange
        Integer roomId = 1;
        User user = new User();
        Rooms room = new Rooms();
        when(roomsRepository.findOne(session, roomId)).thenReturn(room);
        doNothing().when(roomsRepository).bookRoom(session, roomId, user);

        // Act
        roomsService.bookARoom(session, roomId, user);

        // Assert
        verify(roomsRepository).bookRoom(session, roomId, user);
    }
}