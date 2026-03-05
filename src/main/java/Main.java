import config.HibernateFactory;
import domain.Users;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.time.LocalDateTime;


public class Main {
    public static void main(String[] args) {
        SessionFactory sessionFactory = HibernateFactory.getSessionFactory();
        Users user = Users.builder()
                .chatId(123456789L)
                .username("john_doe")
                .age(25)
                .weight(75.5)
                .height(180.0)
                .goal("Набрать массу")
                .createdAt(LocalDateTime.now())  // если нет @CreationTimestamp
                .build();

        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();
            session.persist(user);
            tx.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
