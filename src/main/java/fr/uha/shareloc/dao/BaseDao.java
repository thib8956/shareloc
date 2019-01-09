package fr.uha.shareloc.dao;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import java.util.Collections;
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
        em.getTransaction().begin();
        em.persist(entity);
        em.flush();
        em.getTransaction().commit();
        return entity;
    }

    public <T> T find(Object id, Class<T> clazz) {
        return getEntityManager().find(clazz, id);
    }

    public <T> List<T> findAll(Class<T> clazz) {
        final EntityManager em = getEntityManager();
        final CriteriaQuery<T> criteriaQuery = em.getCriteriaBuilder().createQuery(clazz);
        criteriaQuery.select(criteriaQuery.from(clazz));
        final List<T> results = em.createQuery(criteriaQuery).getResultList();
        if (results == null) {
            return Collections.emptyList();
        }
        return results;
    }

    public <T> T update(T entite) {
        getEntityManager().getTransaction().begin();
        final T updated = getEntityManager().merge(entite);
        getEntityManager().getTransaction().commit();
        return updated;
    }

    public <T> void remove(T entity) {
        getEntityManager().getTransaction().begin();
        final T t = getEntityManager().merge(entity);
        getEntityManager().remove(t);
        getEntityManager().getTransaction().commit();
    }

    public <T> long count(Class<T> clazz) {
        final CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        final CriteriaQuery<Long> cq = cb.createQuery(Long.class);
        cq.select(cb.count(cq.from(clazz)));
        return entityManager.createQuery(cq).getSingleResult();
    }
}
