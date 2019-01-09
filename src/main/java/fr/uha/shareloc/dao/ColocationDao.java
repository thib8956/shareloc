package fr.uha.shareloc.dao;

import fr.uha.shareloc.model.Account;
import fr.uha.shareloc.model.Colocation;
import fr.uha.shareloc.model.Service;
import fr.uha.shareloc.model.User;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;

public class ColocationDao extends BaseDao {

    ColocationDao(EntityManager entityManager) {
        super(entityManager);
    }

    Colocation findColocationForService(int serviceId) {
        final CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        final CriteriaQuery<Colocation> cq = cb.createQuery(Colocation.class);
        final Join<Colocation, Service> ss = cq.from(Colocation.class).join("services");
        cq.where(cb.equal(ss.get("id"), serviceId));
        return entityManager.createQuery(cq).getSingleResult();
    }

    public Service find(Integer serviceId) {
        return super.find(serviceId, Service.class);
    }

    public boolean inviteUser(int colocId, final String userId) {
        final Colocation colocation = find(colocId, Colocation.class);
        final User user = find(userId, User.class);
        if (colocation == null || user == null) return false;
        create(new Account(user, colocation, 0));
        return true;
    }

    public boolean createColocation(String adminLogin, String name) {
        final User admin = find(adminLogin, User.class);
        if (admin == null) return false;
        create(new Colocation(name, admin));
        return true;
    }
}
