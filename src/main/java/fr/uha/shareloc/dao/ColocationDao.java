package fr.uha.shareloc.dao;

import fr.uha.shareloc.model.Account;
import fr.uha.shareloc.model.Colocation;
import fr.uha.shareloc.model.Service;
import fr.uha.shareloc.model.User;

import javax.persistence.EntityManager;

public class ColocationDao extends BaseDao {

    ColocationDao(EntityManager entityManager) {
        super(entityManager);
    }

    public Service findService(Integer serviceId) {
        return find(serviceId, Service.class);
    }

    public boolean inviteUser(int colocId, final String userId) {
        final Colocation colocation = find(colocId, Colocation.class);
        final User user = find(userId, User.class);
        if (colocation == null || user == null) return false;
        create(new Account(user, colocation, 0));
        return true;
    }
}
