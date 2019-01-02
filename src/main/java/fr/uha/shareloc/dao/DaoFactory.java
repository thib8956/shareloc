package fr.uha.shareloc.dao;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class DaoFactory {

    private static final String UNIT_NAME = "sharelocPU";
    private static EntityManagerFactory emf = Persistence.createEntityManagerFactory(UNIT_NAME);
    private static EntityManager em = emf.createEntityManager();

    public static BaseDao createBaseDao() {
        return new BaseDao(em);
    }

    public static ColocationDao createColocationDao() {
        return new ColocationDao(em);
    }

    public static UsersDao createUsersDao() {
        return new UsersDao(em);
    }

}
