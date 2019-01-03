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

    private final CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
    // Update all accounts
    private EntityTransaction transaction = getEntityManager().getTransaction();

    AccountDao(EntityManager entityManager) {
        super(entityManager);
    }

    Account findAccount(User u, Colocation c) {
        final CriteriaQuery<Account> cq = cb.createQuery(Account.class);
        final Root<Account> account = cq.from(Account.class);
        cq.select(account).where(cb.and(
                cb.equal(account.get("user"), u),
                cb.equal(account.get("colocation"), c)
        ));
        return getSingleResultOrNull(getEntityManager().createQuery(cq));
    }

    // TODO test this method
    private List<Account> findAllAccounts(List<User> users, Colocation c) {
        final CriteriaQuery<Account> cq = cb.createQuery(Account.class);
        final Root<Account> account = cq.from(Account.class);
        cq.where(cb.and(
                account.get("user").in(users),
                cb.equal(account.get("colocation"), c)
        ));
        return getEntityManager().createQuery(cq).getResultList();
    }

    public void updateAccounts(User fromUser, List<User> recipients, Service service, Colocation coloc) {
        final int serviceCost = service.getCost();
        final List<Account> recipientsAccounts = findAllAccounts(recipients, coloc);
        int pointsPerUser = serviceCost / recipients.size();
        final Account fromAccount = findAccount(fromUser, coloc);
        fromAccount.addPoints(serviceCost);
        recipientsAccounts.forEach(a -> a.removePoints(pointsPerUser));
        // Update all entities in the database
        transaction.begin();
        getEntityManager().merge(fromAccount);
        recipientsAccounts.forEach(a -> getEntityManager().merge(a));
        transaction.commit();
    }

}
