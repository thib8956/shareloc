package fr.uha.shareloc.dao;

import fr.uha.shareloc.model.AchievedService;
import fr.uha.shareloc.model.Colocation;
import fr.uha.shareloc.model.Service;
import fr.uha.shareloc.model.User;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;

public class UsersDao extends BaseDao {

    UsersDao(EntityManager entityManager) {
        super(entityManager);
    }

    public Colocation findColocationForService(int serviceId) {
        final EntityManager em = getEntityManager();
        final CriteriaBuilder cb = em.getCriteriaBuilder();
        final CriteriaQuery<Colocation> cq = cb.createQuery(Colocation.class);
        final Join<Colocation, Service> ss = cq.from(Colocation.class).join("services");
        cq.where(cb.equal(ss.get("id"), serviceId));
        return em.createQuery(cq).getSingleResult();
    }

    private boolean hasSameColocation(User user, Service service) {
        final AccountDao accountDao = DaoFactory.createAccountDao();
        final Colocation coloc = findColocationForService(service.getId());
        return accountDao.findAccount(user, coloc) != null;
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
