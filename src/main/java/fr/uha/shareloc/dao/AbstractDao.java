package fr.uha.shareloc.dao;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.criteria.CriteriaQuery;
import java.util.Collections;
import java.util.List;

/**
 * This class was given by Mr Wemmert in the P52 UE
 *
 * @author Cedric Wemmert
 */
public class AbstractDao<T> {

    private static final String UNIT_NAME = "sharelocPU";
    private static EntityManagerFactory emf = Persistence.createEntityManagerFactory(UNIT_NAME);
    private static EntityManager entityManager = null;

    private Class<T> clazz;

    public AbstractDao(Class<T> clazz) {
        this.clazz = clazz;
    }

    /**
     * Methode abstraite a definir dans chaque sous-classe qui renvoie
     * l'EntityManager correspondant a la classe.
     *
     * @return l'entity manager
     */
    protected EntityManager getEntityManager() {
        if (entityManager == null) {
            entityManager = emf.createEntityManager();
        }
        return entityManager;
    }

    /**
     * Methode de creation d'un objet (ajout dans la base).
     *
     * @param entite
     */
    public T create(T entite) {
        final EntityManager em = getEntityManager();
        em.getTransaction().begin();
        em.persist(entite);
        em.flush();
        em.getTransaction().commit();
        return entite;
    }

    /**
     * Methode de modification d'un objet.
     *
     * @param entite
     */
    public <U> void edit(U entite) {
        final EntityManager em = getEntityManager();
        em.getTransaction().begin();
        em.merge(entite);
        em.getTransaction().commit();
    }

    /**
     * Methode de suppression d'un objet.
     *
     * @param entity
     */
    public <U> void remove(U entity) {
        getEntityManager().getTransaction().begin();
        final U u = getEntityManager().merge(entity);
        getEntityManager().remove(u);
        getEntityManager().getTransaction().commit();
    }

    /**
     * Methode de recherche d'un objet a partir de son identifiant.
     *
     * @param id
     * @return
     * @return
     */
    public T find(Object id) {
        return getEntityManager().find(clazz, id);
    }

    public <U> U find(Object id, Class<U> clazz) {
        return getEntityManager().find(clazz, id);
    }

    /**
     * Methode recherchant tous les objets de ce type.
     *
     * @return
     */
    public List<T> findAll() {
        final EntityManager em = getEntityManager();
        final CriteriaQuery<T> criteriaQuery = em.getCriteriaBuilder().createQuery(clazz);
        criteriaQuery.select(criteriaQuery.from(clazz));
        final List<T> results = em.createQuery(criteriaQuery).getResultList();
        if (results == null) {
            return Collections.emptyList();
        }
        return results;
    }

    /**
     * Methode renvoyant le nombre d'objet de ce type.
     *
     * @return
     */
    public long count() {
        final EntityManager em = getEntityManager();
        final CriteriaQuery<Long> cq = em.getCriteriaBuilder().createQuery(Long.class);
        final javax.persistence.criteria.Root<Long> rt = cq.from(Long.class);
        cq.select(em.getCriteriaBuilder().count(rt));
        javax.persistence.Query q = em.createQuery(cq);
        return (Long) q.getSingleResult();
    }
}
