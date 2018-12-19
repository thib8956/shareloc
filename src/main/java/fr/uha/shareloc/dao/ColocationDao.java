package fr.uha.shareloc.dao;

import fr.uha.shareloc.model.Account;
import fr.uha.shareloc.model.Colocation;
import fr.uha.shareloc.model.Service;
import fr.uha.shareloc.model.User;

public class ColocationDao extends BaseDao {

    public ColocationDao() {}

    public Service findService(Integer serviceId) {
        return find(serviceId, Service.class);
    }

    public boolean inviteUser(int colocId, int userId) {
        final Colocation colocation =find(colocId, Colocation.class);
        final User user = find(userId, User.class);
        if (colocation == null || user == null) return false;
        create(new Account(user, colocation, 0));
        return true;
    }
}
