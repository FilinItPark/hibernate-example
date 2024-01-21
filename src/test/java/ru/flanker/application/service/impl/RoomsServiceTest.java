package ru.flanker.application.service.impl;

import jakarta.persistence.EntityNotFoundException;
import org.hibernate.Session;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ru.flanker.application.service.RoomsService;
import ru.flanker.domain.model.Rooms;
import ru.flanker.domain.model.User;
import ru.flanker.domain.repository.RoomsRepository;

import java.util.List;
import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RoomsServiceTest {

    @Mock
    private RoomsRepository roomsRepository;

    @Mock
    private Session session;

    private RoomsService roomsService;

    private List<String> strings;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        roomsService = new RoomsServiceImpl(roomsRepository);

//        strings = new ArrayList<>();
//        for (int i=0;i<100;i++) {
//            String randomString = generateRandomString(50);
//            strings.add(randomString);
//        }
//        strings.add("ewewew");

    }

    //    @Test
//    void check_Is_EveryString_HasLength_50() {
//        for (var a : strings) {
//            assertEquals(50, a.length());
//        }
//    }
 /*   public static String generateRandomString(int length) {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890";
        StringBuilder randomString = new StringBuilder(length);
        Random rand = new Random();
        for (int i = 0; i < length; i++) {
            randomString.append(characters.charAt(rand.nextInt(characters.length())));
        }
        return randomString.toString();
    }*/

    /* запрещено
    1) try catch
    2) циклы
    3) условия
     */
    @Test
    void getAllRooms_ShouldReturnListOfRooms() {
        // Arrange
        List<Rooms> expectedRooms = List.of(new Rooms(2,"dasdasdsa", new User()), new Rooms());
        when(roomsRepository.findAll(session)).thenReturn(expectedRooms);

        // Act проверяем что в рум сервисе юзается рум репозитории и не происходит никаких побочных эффектов в результате получения наших данных
        // условно, что мы релаьно дергаем данные из бд, а не возвращаем статичный массив
        List<Rooms> actualRooms = roomsService.getAllRooms(session);

        // Assert
        assertEquals(expectedRooms, actualRooms);
        verify(roomsRepository).findAll(session);
    }

    @Test
    @DisplayName("Checking function of sum")
    void checkSum_IsGreaterZero_And_Sum5_And_3_Is_Equals_8() {
        //arrange
        int a = 5;
        int b = 3;

        int result = 8;

        //act
        int c = a + b;

        //assert

        assertEquals(result, c);
        assertThrows(IllegalAccessError.class, () -> {
            throw new Exception("hello");
        }, "Проверка на то что вылетит исключение Exception");

        assertTrue(c > 0);
        assertThat(c).isGreaterThan(0);
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
    void deleteRoom_shouldThrowException() {
        int roomId = 2;
        when(roomsRepository.findOne(session, roomId)).thenReturn(null);

        assertThrows(EntityNotFoundException.class, () -> {
           roomsService.deleteRoom(roomId);
        });
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