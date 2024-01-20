package ru.flanker;

import ru.flanker.domain.model.Rooms;
import ru.flanker.domain.model.User;
import ru.flanker.domain.model.enums.UserRoles;
import ru.flanker.domain.repository.impl.RoomsRepositoryImpl;
import ru.flanker.domain.repository.impl.UserRepositoryImpl;
import ru.flanker.infrastructure.HibernateInitializer;

/**
 * @author 1ommy
 * @version 14.01.2024
 */
public class Main {
    public static void main(String[] args) {
        try (
                var sessionBuilder = HibernateInitializer.getSessionFactory();
                var session = sessionBuilder.openSession()
        ) {
            var userRepository = new UserRepositoryImpl();
            var roomRepository = new RoomsRepositoryImpl();

            userRepository.save(session,
                    User.builder()
                            .fullName("Ivan Berezutskij")
                            .username("1ommy")
                            .role(UserRoles.USER)
                            .build()
            );

            userRepository.save(session,
                    User.builder()
                            .fullName("Alexey Smyslov")
                            .username("itpark")
                            .role(UserRoles.USER)
                            .build()
            );
            userRepository.save(session,
                    User.builder()
                            .fullName("Vladimir Putin")
                            .username("putin")
                            .role(UserRoles.ADMIN)
                            .build()
            );
            userRepository.save(session,
                    User.builder()
                            .fullName("Nikolay Romanov")
                            .username("romanov")
                            .role(UserRoles.TEACHER)
                            .build()
            );

            roomRepository.save(session,
                    Rooms.builder()
                            .number(1)
                            .description("room 1")
                            .bookedBy(
                                    userRepository.findOne(session, 1)
                            )
                            .build()
            );


            roomRepository.save(session,
                    Rooms.builder()
                            .number(253)
                            .description("room 253")
                            .bookedBy(
                                    userRepository.findOne(session, 1)
                            )
                            .build()
            );


            roomRepository.save(session,
                    Rooms.builder()
                            .number(512)
                            .description("room 512")
                            .bookedBy(
                                    userRepository.findOne(session, 3)
                            )
                            .build()
            );
        }
    }
}