package ru.flanker.infrastructure;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import ru.flanker.domain.model.BaseClass;
import ru.flanker.domain.model.DateInformation;
import ru.flanker.domain.model.Rooms;
import ru.flanker.domain.model.User;

/**
 * @author 1ommy
 * @version 14.01.2024
 */
public class HibernateInitializer {
    private static SessionFactory sessionFactory;

    public static SessionFactory getSessionFactory() {
        if (sessionFactory == null) {
            sessionFactory = new Configuration()
                    .addAnnotatedClass(Rooms.class)
                    .addAnnotatedClass(User.class)
                    .addAnnotatedClass(DateInformation.class)
                    .addAnnotatedClass(BaseClass.class)
                    .configure().buildSessionFactory();
        }
        return sessionFactory;
    }
}
