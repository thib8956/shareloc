package fr.uha.shareloc.dao;

import fr.uha.shareloc.model.AchievedService;
import fr.uha.shareloc.model.Colocation;
import fr.uha.shareloc.model.Service;
import fr.uha.shareloc.model.User;

import javax.persistence.EntityManager;

public class UsersDao extends BaseDao {

    UsersDao(EntityManager entityManager) {
        super(entityManager);
    }

    private boolean hasSameColocation(User user, Service service) {
        final AccountDao accountDao = DaoFactory.createAccountDao();
        final ColocationDao colocationDao = DaoFactory.createColocationDao();
        final Colocation coloc = colocationDao.findColocationForService(service.getId());
        return accountDao.findAccount(user, coloc) != null;
    }

    public QueryStatus reserveService(String login, int serviceId) {
        final User user = find(login, User.class);
        final Service service = find(serviceId, Service.class);
        if (service == null) return new QueryStatus(false, "Service " + serviceId + " not found");
        if (user == null) return new QueryStatus(false, "User " + login + "not found");
        // Check if the service is already reserved
        if (service.getFrom() != null) {
            return new QueryStatus(false, "Service already reserved");
        }
        // Check if the user and the service are in the same colocation
        if (!hasSameColocation(user, service)) {
            return new QueryStatus(false, "The user and the service must be in the same colocation");
        }

        service.setFrom(user);
        update(service);
        return new QueryStatus(true);
    }

    public QueryStatus realizeService(String login, int serviceId) {
        final User user = find(login, User.class);
        final Service service = find(serviceId, Service.class);
        if (service == null) return new QueryStatus(false, "Service " + serviceId + " not found");
        if (user == null) return new QueryStatus(false, "User " + login + "not found");
        // Check if the user has reserved the service
        if (service.getFrom() == null || service.getFrom() != user) {
            return new QueryStatus(false, "User " + login + " has not reserved this service");
        }
        // Make the service an AchievedService
        final AchievedService as = new AchievedService(user, service.getRecipients(), service);
        service.setAchieved(true);
        update(service);
        create(as);
        return new QueryStatus(true);
    }

    public static class QueryStatus {
        public final boolean success;
        public final String message;

        QueryStatus(boolean success, String message) {
            this.success = success;
            this.message = message;
        }

        QueryStatus(boolean success) {
            this(success, "");
        }
    }
}
