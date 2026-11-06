package dao;

import model.User;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import util.HibernateUtil;

public class UserDAO {

    public User login(String username, String password) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<User> query = session.createQuery(
                    "from User where username = :username and password = :password",
                    User.class);
            query.setParameter("username", username);
            query.setParameter("password", password);
            return query.uniqueResult();
        } catch (RuntimeException ex) {
            throw new RuntimeException("Gagal memvalidasi akun: " + ex.getMessage(), ex);
        }
    }

    public boolean usernameExists(String username) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Long> query = session.createQuery(
                    "select count(u) from User u where u.username = :username",
                    Long.class);
            query.setParameter("username", username);
            Long jumlah = query.uniqueResult();
            return jumlah != null && jumlah > 0;
        } catch (RuntimeException ex) {
            throw new RuntimeException("Gagal memeriksa username: " + ex.getMessage(), ex);
        }
    }

    public User insert(User user) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            if (user.getRole() == null || user.getRole().isBlank()) {
                user.setRole("user");
            }
            session.persist(user);
            transaction.commit();
            return user;
        } catch (RuntimeException ex) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            throw new RuntimeException("Gagal menyimpan akun: " + ex.getMessage(), ex);
        }
    }
}
