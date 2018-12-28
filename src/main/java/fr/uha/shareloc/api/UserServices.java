package fr.uha.shareloc.api;

import fr.uha.shareloc.dao.BaseDao;
import fr.uha.shareloc.model.Service;
import fr.uha.shareloc.model.User;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

@Path("/users")
public class UserServices extends BaseServices<User> {

    protected UserServices() {
        super(new BaseDao(), User.class);
    }

    @POST
    @Path("vote")
    public Response voteForService(@QueryParam("serviceId") int serviceId, @QueryParam("vote") int vote) {
        final Service s = getDao().find(serviceId, Service.class);

        if (vote != 1 && vote != -1) return Response.status(500).build();
        if (s == null) return Response.status(Response.Status.NOT_FOUND).build();
        final int voteCount = s.vote(vote);
        final long usersCount = getDao().count(User.class);
        getDao().update(s);

        if (voteCount == usersCount) countVotes(s, usersCount);
        return Response.ok().build();
    }

    private void countVotes(Service s, long usersCount) {
        if (s.getUpvotes() > usersCount / 2) {
            s.setAccepted();
            getDao().update(s);
        } else {
            getDao().remove(s);
        }
    }


}
