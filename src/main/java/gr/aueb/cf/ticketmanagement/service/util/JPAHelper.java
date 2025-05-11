package gr.aueb.cf.ticketmanagement.service.util;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

public class JPAHelper {
    private static EntityManagerFactory emf;
    private static final ThreadLocal<EntityManager> threadlocal = new ThreadLocal<>();

    private static final HikariConfig config = new HikariConfig();
    private static final Map<String, Object> properties = new HashMap<>();

    static {
        config.setJdbcUrl(System.getenv("TICKET_DB_HOST") + ":" + System.getenv("TICKET_DB_PORT") +
                "/" + System.getenv("TICKET_DB_DATABASE") + "?serverTimezone=UTC");
        config.setUsername(System.getenv("USERNAME_DB"));
        config.setPassword(System.getenv("PASSWORD_DB"));
        DataSource dataSource = new HikariDataSource(config);
        properties.put("hibernate.connection.datasource", dataSource);
    }

    private JPAHelper() {

    }

    public static EntityManagerFactory getEntityManagerFactory() {
        if (emf == null || !emf.isOpen()) emf = Persistence.createEntityManagerFactory("ticketContext", properties);
        return emf;
    }

    public static EntityManager getEntityManager() {
        EntityManager em = threadlocal.get();
        if (em == null || !em.isOpen()) {
            em = getEntityManagerFactory().createEntityManager();
            threadlocal.set(em);
        }
        return em;
    }

    public static void closeEntityManager() {
        getEntityManager().close();
        threadlocal.remove();
    }

    public static void beginTransaction() {
        getEntityManager().getTransaction().begin();
    }

    public static void commitTransaction() {
        getEntityManager().getTransaction().commit();
    }

    public static void rollbackTransaction() {
        getEntityManager().getTransaction().rollback();
    }

    public static void closeEntityManagerFactory() {
        emf.close();
    }
}
