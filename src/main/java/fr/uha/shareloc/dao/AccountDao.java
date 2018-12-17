package fr.uha.shareloc.dao;

import fr.uha.shareloc.model.Account;
import fr.uha.shareloc.model.Colocation;
import fr.uha.shareloc.model.User;

public class AccountDao extends AbstractDao<Account> {

    public AccountDao() {
        super(Account.class);
    }

    public boolean inviteUser(int colocId, int userId) {
        final Colocation colocation = getEntityManager().find(Colocation.class, colocId);
        final User user = getEntityManager().find(User.class, userId);
        if (colocation == null || user == null) return false;
        create(new Account(user, colocation, 0));
        return true;
    }
}
