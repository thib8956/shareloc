package fr.uha.shareloc.dao;

import fr.uha.shareloc.model.Colocation;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.criteria.CriteriaQuery;
import java.util.List;

public class ColocationDao extends AbstractDao<Colocation> {

    public ColocationDao() {
        super(Colocation.class);
    }

}
