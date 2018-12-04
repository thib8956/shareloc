package fr.uha.shareloc.dao;

import fr.uha.shareloc.model.Colocation;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.List;

public class ColocationDao {

    private static final String UNIT_NAME = "sharelocPU";
    private EntityManager entityManager;

    public ColocationDao() {
        final EntityManagerFactory emf = Persistence.createEntityManagerFactory(UNIT_NAME);
        this.entityManager = emf.createEntityManager();
    }

    public List<Colocation> getColocations() {
        return entityManager
                .createQuery("SELECT c FROM Colocation c", Colocation.class)
                .getResultList();
    }
}
