package fr.uha.shareloc.api;

import fr.uha.shareloc.dao.*;
import fr.uha.shareloc.model.AchievedService;
import fr.uha.shareloc.model.User;

import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/achieved")
public class AchievedServiceServices extends BaseServices<AchievedService> {

    protected AchievedServiceServices() {
        super(DaoFactory.createBaseDao(), AchievedService.class);
    }

    @Path("validate")
    @POST
    public Response validateAchievedService(@FormParam("login") String login, @FormParam("serviceId") int serviceId) {
        final BaseDao dao = getDao();
        final User user = dao.find(login, User.class);
        final AchievedService achievedService = dao.find(serviceId, AchievedService.class);

        if (user == null || achievedService == null) return Response.status(Response.Status.NOT_FOUND).build();

        // Check if the AchievedService is already validated
        if (achievedService.isValid()) {
            return Response.status(Response.Status.UNAUTHORIZED)
                    .entity("The achieved service " + achievedService + "is already validated.")
                    .build();
        }

        final User fromUser = achievedService.getFrom();
        final List<User> recipients = achievedService.getTo();

        // The AchievedService must be validated by one of its recipients.
        if (!recipients.contains(user)) {
            return Response.status(Response.Status.UNAUTHORIZED)
                    .entity(user + " is not a recipient of " + achievedService)
                    .build();
        }
        // Validate the service
        achievedService.setValid(true);
        dao.update(achievedService);

        // Once this declaration has been validated, the associated cost is credited to the account of the user
        // who performed the service, and debited in a shared manner from the other colocation
        // members who benefited from it.
        final AccountDao accountDao = DaoFactory.createAccountDao();
        accountDao.updateAccountsWithService(fromUser, recipients, achievedService.getService());

        return Response.ok(achievedService).build();
    }
}
