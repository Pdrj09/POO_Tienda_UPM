package etsisi.upm.util;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.util.logging.Level;
import java.util.logging.Logger;

public class HibernateUtil {

    static {
        // Silenciar logs de Hibernate
        Logger.getLogger("org.hibernate").setLevel(Level.OFF);
    }

    private static final SessionFactory sessionFactory = buildSessionFactory();

    private static SessionFactory buildSessionFactory() {
        try {
            return new Configuration().configure().buildSessionFactory();
        } catch (Throwable ex) {
            System.err.println(Constants.ERROR_SESSION_CREATION + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public static void shutdown() {
        getSessionFactory().close();
    }
}
