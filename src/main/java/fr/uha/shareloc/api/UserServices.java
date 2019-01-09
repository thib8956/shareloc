package fr.uha.shareloc.api;

import fr.uha.shareloc.dao.BaseDao;
import fr.uha.shareloc.model.Service;
import fr.uha.shareloc.model.User;

import javax.json.JsonObject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/users")
public class UserServices extends BaseServices<User> {

    protected UserServices() {
        super(new BaseDao(), User.class);
    }

    @POST
    @Path("/vote")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response voteForService(JsonObject params) {
        final int vote = params.getInt("vote");
        final int serviceId = params.getInt("serviceId");
        final Service s = getDao().find(serviceId, Service.class);

        if (vote != 1 && vote != -1) return Response.status(500).build();
        if (s == null) return Response.status(Response.Status.NOT_FOUND).build();
        final int voteCount = s.vote(vote);
        // TODO use dao.count() instead
        final long usersCount = getDao().findAll(User.class).size();
        getDao().update(s);

        if (voteCount == usersCount) countVotes(s, usersCount);
        return Response.ok().build();
    }

    private void countVotes(Service s, long usersCount) {
        if (s.getUpvotes() > usersCount/2) {
            s.setAccepted();
            getDao().update(s);
        } else {
            getDao().remove(s);
        }
    }


}
