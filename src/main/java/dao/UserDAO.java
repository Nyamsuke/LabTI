package dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.PersistenceUnit;
import org.springframework.stereotype.Repository;
import model.User;

@Repository 
public class UserDAO {

    @PersistenceUnit 
    private EntityManagerFactory entityManagerFactory;

    public void setEntityManagerFactory(EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
    }

    public User login(String username, String password) {
        EntityManager em = entityManagerFactory.createEntityManager();
        try {
            TypedQuery<User> query = em.createQuery(
                    "SELECT u FROM User u WHERE u.username = :username AND u.password = :password",
                    User.class
            );
            query.setParameter("username", username);
            query.setParameter("password", password);
            return query.getResultStream().findFirst().orElse(null);
        } finally {
            em.close();
        }
    }

    public boolean usernameExists(String username) {
        EntityManager em = entityManagerFactory.createEntityManager();
        try {
            Long jumlah = em.createQuery(
                    "SELECT COUNT(u) FROM User u WHERE u.username = :username",
                    Long.class
            )
            .setParameter("username", username)
            .getSingleResult();
            return jumlah > 0;
        } finally {
            em.close();
        }
    }

    public User insert(User user) {
        EntityManager em = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = em.getTransaction();
        try {
            transaction.begin();
            if (user.getRole() == null || user.getRole().isBlank()) {
                user.setRole("user");
            }
            em.persist(user);
            transaction.commit();
            return user;
        } catch (RuntimeException ex) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            throw ex;
        } finally {
            em.close();
        }
    }
}