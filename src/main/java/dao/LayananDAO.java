package dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.PersistenceUnit;
import java.util.List;
import model.Layanan;
import org.springframework.stereotype.Repository;

@Repository
public class LayananDAO {

    @PersistenceUnit
    private EntityManagerFactory entityManagerFactory;

    public void setEntityManagerFactory(EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
    }

    public List<Layanan> findAll() {
        EntityManager em = entityManagerFactory.createEntityManager();
        try {
            return em.createQuery("SELECT l FROM Layanan l ORDER BY l.id", Layanan.class)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    public List<Layanan> findAktif() {
        EntityManager em = entityManagerFactory.createEntityManager();
        try {
            return em.createQuery(
                    "SELECT l FROM Layanan l WHERE l.status = 'Aktif' ORDER BY l.id",
                    Layanan.class)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    public Layanan findById(int id) {
        EntityManager em = entityManagerFactory.createEntityManager();
        try {
            return em.find(Layanan.class, id);
        } finally {
            em.close();
        }
    }

    public boolean namaExists(String namaLayanan, int excludeId) {
        EntityManager em = entityManagerFactory.createEntityManager();
        try {
            Long jumlah = em.createQuery(
                    "SELECT COUNT(l) FROM Layanan l "
                    + "WHERE l.namaLayanan = :nama AND l.id <> :excludeId",
                    Long.class)
                    .setParameter("nama", namaLayanan)
                    .setParameter("excludeId", excludeId)
                    .getSingleResult();
            return jumlah != null && jumlah > 0;
        } finally {
            em.close();
        }
    }

    public Layanan save(Layanan layanan) {
        EntityManager em = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = em.getTransaction();
        try {
            transaction.begin();
            Layanan hasil;
            if (layanan.getId() == 0) {
                em.persist(layanan);
                hasil = layanan;
            } else {
                hasil = em.merge(layanan);
            }
            transaction.commit();
            return hasil;
        } catch (RuntimeException ex) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            throw ex;
        } finally {
            em.close();
        }
    }

    public void delete(int id) {
        EntityManager em = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = em.getTransaction();
        try {
            transaction.begin();
            Layanan layanan = em.find(Layanan.class, id);
            if (layanan != null) {
                em.remove(layanan);
            }
            transaction.commit();
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
