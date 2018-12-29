package fr.uha.shareloc.dao;

import fr.uha.shareloc.model.*;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Root;
import java.util.Optional;

public class UsersDao extends BaseDao {

    private Colocation findColocationForService(int serviceId) {
        final EntityManager em = getEntityManager();
        final CriteriaBuilder cb = em.getCriteriaBuilder();
        final CriteriaQuery<Colocation> cq = cb.createQuery(Colocation.class);
        final Join<Colocation, Service> ss = cq.from(Colocation.class).join("services");
        cq.where(cb.equal(ss.get("id"), serviceId));
        return em.createQuery(cq).getSingleResult();
    }

    private Optional<Account> findAccount(User user, Colocation colocation) {
        final EntityManager em = getEntityManager();
        final CriteriaBuilder cb = em.getCriteriaBuilder();
        final CriteriaQuery<Account> cq = cb.createQuery(Account.class);
        final Root<Account> account = cq.from(Account.class);
        cq.select(account).where(cb.and(
                cb.equal(account.get("user"), user),
                cb.equal(account.get("colocation"), colocation)
        ));

        return em.createQuery(cq).getResultList().stream().findFirst();
    }

    private boolean hasSameColocation(User user, Service service) {
        final Colocation coloc = findColocationForService(service.getId());
        return findAccount(user, coloc).isPresent();
    }

    public boolean reserveService(String login, int serviceId) {
        final User user = find(login, User.class);
        final Service service = find(serviceId, Service.class);
        // TODO: return error values instead of a boolean
        // Check if the service is already reserved
        if (service.getFrom() != null) return false;
        // Check if the user and the service are in the same colocation
        if (!hasSameColocation(user, service)) return false;
        service.setFrom(user);
        update(service);
        return true;

    }

    public boolean realizeService(String login, int serviceId) {
        final User user = find(login, User.class);
        final Service service = find(serviceId, Service.class);
        // TODO: return error values instead of a boolean
        // Check if the user has reserved the service
        if (service.getFrom() == null || service.getFrom() != user) return false;
        // Make the service an AchievedService
        final AchievedService as = new AchievedService(user, service.getRecipients(), service);
        service.setAchieved(true);
        update(service);
        create(as);
        return true;
    }
}
