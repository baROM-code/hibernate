package org.geekbrains.ru;

import org.flywaydb.core.Flyway;
import org.geekbrains.ru.domain.SimpleItem;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.util.List;

public class Main {

    public static SessionFactory factory;

    private static void init() {
        factory = new Configuration()
                .configure("config/hibernate.cfg.xml")
                .buildSessionFactory();
    }

    private static void shutdown() {
        factory.close();
    }

    public static void main(String[] args) {
//        Flyway flyway = Flyway.configure()
//                .dataSource("jdbc:postgresql://localhost:5432/simple-app", "postgres", "postgrespass").load();
//        flyway.migrate();
        try {
            init();
            getCollectionSimpleItems();
        } finally {
            shutdown();
        }
    }

    // ACID
    public static void createSimpleItem() {
        try (Session session = factory.getCurrentSession()) {
            session.beginTransaction();
            SimpleItem item = new SimpleItem("Gift 1", 200);
            System.out.println(item);
            session.save(item);
            System.out.println(item);
            session.getTransaction().commit();
            System.out.println(item);
        }
    }

    public static void getSimpleItem() {
        try (Session session = factory.getCurrentSession()) {
            session.beginTransaction();

            SimpleItem item = session.get(SimpleItem.class, 1L);
            System.out.println(item);

            session.getTransaction().commit();
        }
    }

    public static void updateSimpleItem() {
        try (Session session = factory.getCurrentSession()) {
            session.beginTransaction();

            SimpleItem item = session.get(SimpleItem.class, 1L);
            item.setPrice(200);

            session.getTransaction().commit();
        }
    }

    public static void deleteSimpleItem() {
        try (Session session = factory.getCurrentSession()) {
            session.beginTransaction();

            SimpleItem item = session.get(SimpleItem.class, 3L);
            session.delete(item);

            session.getTransaction().commit();
        }
    }

    public static void getCollectionSimpleItems() {
        try (Session session = factory.getCurrentSession()) {
            session.beginTransaction();

            List<SimpleItem> resultList = session.createQuery("select s from SimpleItem s where s.price <= 100", SimpleItem.class)
                    .getResultList();

            System.out.println(resultList);
            session.getTransaction().commit();
        }
    }

}
