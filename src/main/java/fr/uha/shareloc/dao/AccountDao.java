package fr.uha.shareloc.dao;

import fr.uha.shareloc.model.Account;
import fr.uha.shareloc.model.Colocation;
import fr.uha.shareloc.model.Service;
import fr.uha.shareloc.model.User;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

import static fr.uha.shareloc.util.JPAHelper.getSingleResultOrNull;

public class AccountDao extends BaseDao {

    private final CriteriaBuilder cb;
    private final EntityTransaction transaction;

    AccountDao(EntityManager entityManager) {
        super(entityManager);
        cb = entityManager.getCriteriaBuilder();
        transaction = entityManager.getTransaction();
    }

    Account findAccount(User u, Colocation c) {
        final CriteriaQuery<Account> cq = cb.createQuery(Account.class);
        final Root<Account> account = cq.from(Account.class);
        cq.select(account).where(cb.and(
                cb.equal(account.get("user"), u),
                cb.equal(account.get("colocation"), c)
        ));
        return getSingleResultOrNull(entityManager.createQuery(cq));
    }

    // TODO test this method
    private List<Account> findAllAccounts(List<User> users, Colocation c) {
        final CriteriaQuery<Account> cq = cb.createQuery(Account.class);
        final Root<Account> account = cq.from(Account.class);
        cq.where(cb.and(
                account.get("user").in(users),
                cb.equal(account.get("colocation"), c)
        ));
        return entityManager.createQuery(cq).getResultList();
    }

    public void updateAccountsWithService(User fromUser, List<User> recipients, Service service) {
        final ColocationDao colocationDao = DaoFactory.createColocationDao();
        final Colocation coloc = colocationDao.findColocationForService(service.getId());
        final int serviceCost = service.getCost();
        final List<Account> recipientsAccounts = findAllAccounts(recipients, coloc);
        int pointsPerUser = serviceCost / recipients.size();
        final Account fromAccount = findAccount(fromUser, coloc);
        fromAccount.addPoints(serviceCost);
        recipientsAccounts.forEach(a -> a.removePoints(pointsPerUser));
        // Update all entities in the database
        transaction.begin();
        entityManager.merge(fromAccount);
        recipientsAccounts.forEach(entityManager::merge);
        transaction.commit();
    }

}
