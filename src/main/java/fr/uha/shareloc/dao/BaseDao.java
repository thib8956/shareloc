package fr.uha.shareloc.dao;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import java.util.List;

public class BaseDao {

    private static final String UNIT_NAME = "sharelocPU";
    private static EntityManagerFactory emf = Persistence.createEntityManagerFactory(UNIT_NAME);
    private static EntityManager entityManager = null;

    private EntityManager getEntityManager() {
        if (entityManager == null) {
            entityManager = emf.createEntityManager();
        }
        return entityManager;
    }

    public <T> T create(T entity) {
        final EntityManager em = getEntityManager();
        final EntityTransaction tx = em.getTransaction();
        tx.begin();
        em.persist(entity);
        em.flush();
        tx.commit();
        return entity;
    }

    public <T> T find(Object id, Class<T> clazz) {
        return getEntityManager().find(clazz, id);
    }

    public <T> List<T> findAll(Class<T> clazz) {
        final EntityManager em = getEntityManager();
        final CriteriaQuery<T> criteriaQuery = em.getCriteriaBuilder().createQuery(clazz);
        criteriaQuery.select(criteriaQuery.from(clazz));
        return em.createQuery(criteriaQuery).getResultList();
    }

    public <T> T update(T entite) {
        final EntityTransaction tx = getEntityManager().getTransaction();
        tx.begin();
        final T updated = getEntityManager().merge(entite);
        tx.commit();
        return updated;
    }

    public <T> void remove(T entity) {
        final EntityManager em = getEntityManager();
        final EntityTransaction tx = em.getTransaction();
        tx.begin();
        final T t = em.merge(entity);
        em.remove(t);
        tx.commit();
    }

    public <T> long count(Class<T> clazz) {
        final CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        final CriteriaQuery<Long> cq = cb.createQuery(Long.class);
        cq.select(cb.count(cq.from(clazz)));
        return entityManager.createQuery(cq).getSingleResult();
    }
}
