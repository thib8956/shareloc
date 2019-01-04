package fr.uha.shareloc.dao;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import java.util.List;

public class BaseDao {

    protected final EntityManager entityManager;
    private CriteriaBuilder criteriaBuilder;

    BaseDao(EntityManager entityManager) {
        this.entityManager = entityManager;
        this.criteriaBuilder = entityManager.getCriteriaBuilder();
    }

    public <T> T create(T entity) {
        final EntityTransaction tx = entityManager.getTransaction();
        tx.begin();
        entityManager.persist(entity);
        entityManager.flush();
        tx.commit();
        return entity;
    }

    public <T> T find(Object id, Class<T> clazz) {
        return entityManager.find(clazz, id);
    }

    public <T> List<T> findAll(Class<T> clazz) {
        final CriteriaQuery<T> criteriaQuery = criteriaBuilder.createQuery(clazz);
        criteriaQuery.select(criteriaQuery.from(clazz));
        return entityManager.createQuery(criteriaQuery).getResultList();
    }

    public <T> T update(T entite) {
        final EntityTransaction tx = entityManager.getTransaction();
        tx.begin();
        final T updated = entityManager.merge(entite);
        tx.commit();
        return updated;
    }

    public <T> void remove(T entity) {
        final EntityTransaction tx = entityManager.getTransaction();
        tx.begin();
        final T t = entityManager.merge(entity);
        entityManager.remove(t);
        tx.commit();
    }

    public <T> long count(Class<T> clazz) {
        final CriteriaQuery<Long> cq = criteriaBuilder.createQuery(Long.class);
        cq.select(criteriaBuilder.count(cq.from(clazz)));
        return entityManager.createQuery(cq).getSingleResult();
    }
}
